package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.LoginInfo;
import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.mapper.RealAuthMapper;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.AsserUtil;
import cn.wolfcode.p2p.util.BitStatesUtils;
import cn.wolfcode.p2p.util.PageResult;
import cn.wolfcode.p2p.util.UserContext;
import com.sun.xml.internal.ws.wsdl.DispatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class RealAuthServiceImpl implements IRealAuthService {

    @Autowired
    private RealAuthMapper realAuthMapper;
    @Autowired
    private IUserInfoService userInfoService;


    public void apply(RealAuth realAuth) {
        //获取当前登录用户对象
        LoginInfo loginInfo = UserContext.getLoginInfo();
        UserInfo userInfo = userInfoService.get(loginInfo.getId());
        //参数判断
        AsserUtil.isTrue(userInfo.getRealAuthId() != null,"您已经有提交中的实名申请");

        //保存实名申请
        RealAuth newRealAuth = new RealAuth();
        newRealAuth.setRealName(realAuth.getRealName());
        newRealAuth.setIdNumber(realAuth.getIdNumber());
        newRealAuth.setAddress(realAuth.getAddress());

        newRealAuth.setApplier(loginInfo);

        newRealAuth.setApplyTime(new Date());
        newRealAuth.setBornDate(realAuth.getBornDate());
        newRealAuth.setImage1(realAuth.getImage1());
        newRealAuth.setImage2(realAuth.getImage2());
        newRealAuth.setSex(realAuth.getSex());
        newRealAuth.setState(RealAuth.STATE_NORMAL);
        realAuthMapper.insert(newRealAuth);

        //把实名申请对象的id设置到userInfo.realAuthId
        userInfo.setRealAuthId(newRealAuth.getId());
        userInfoService.update(userInfo);
    }

    @Override
    public RealAuth get(Long realAuthId) {
        return realAuthMapper.selectByPrimaryKey(realAuthId);
    }

    @Override
    public PageResult query(RealAuthQueryObject qo) {
        int count = realAuthMapper.queryForCount(qo);
        if (count==0) {
            return PageResult.empty(qo.getPageSize());
        }
        return new PageResult(realAuthMapper.queryForList(qo),count,qo.getCurrentPage(),qo.getPageSize());
    }


    public void audit(Long id, int state, String remark) {
        //判断实名认证对象是否处于待审核状态
        RealAuth realAuth = realAuthMapper.selectByPrimaryKey(id);
        if (realAuth.getState() != RealAuth.STATE_NORMAL) {
            throw new DisplayableException("实名认证对象状态不处于待审核");
        }
        //进行实名审核
        //设置审核相关信息
        realAuth.setAuditor(UserContext.getLoginInfo());
        realAuth.setAuditTime(new Date());
        realAuth.setRemark(remark);
        realAuth.setState(state);
        update(realAuth);
        //获取用户的userInfo
        UserInfo userInfo = userInfoService.get(realAuth.getApplier().getId());
        //审核通过
        if(state == RealAuth.STATE_SUCCESS){
            //修改userInfo.bitState加上实名认证的状态
            if (userInfo.isRealAuth()){
                throw new DisplayableException("用户已经完成实名认证");
            }
            Long tempState = BitStatesUtils.addState(userInfo.getBitState(), BitStatesUtils.OP_REAL_AUTH);
            userInfo.setBitState(tempState);
        }else {
            //审核拒绝
            //修改userInfo.realAuthId 设置为null
            userInfo.setRealAuthId(null);
        }

        userInfoService.update(userInfo);
    }

    private void update(RealAuth realAuth) {
        int count = realAuthMapper.updateByPrimaryKey(realAuth);
        if (count == 0){
            throw new DisplayableException("实名信息修改");
        }
    }
}
