package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.UserInfo;
import cn.wolfcode.p2p.base.exception.DisplayableException;
import cn.wolfcode.p2p.base.mapper.UserInfoMapper;
import cn.wolfcode.p2p.base.service.IUserInfoService;
import cn.wolfcode.p2p.util.BitStatesUtils;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    public void init(Long id,String phoneNumber) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setPhoneNumber(phoneNumber);
        userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo get(Long id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public void basicInfoSave(UserInfo userInfo) {
        //基本判断
        //保存基本资料
        Long userId = UserContext.getLoginInfo().getId();
        userInfo.setId(userId);

        //对于前端传输过来的数据,需要仔细地做判断,防止有人恶意携带参数访问,
        //所有根据当前用户的id从数据库中查询出对应的记录,再重新保存到数据库中
        UserInfo info = userInfoMapper.selectByPrimaryKey(userId);
        info.setEducationBackground(userInfo.getEducationBackground());
        info.setHouseCondition(userInfo.getHouseCondition());
        info.setIncomeGrade(userInfo.getIncomeGrade());
        info.setKidCount(userInfo.getKidCount());
        info.setMarriage(userInfo.getMarriage());

        //userInfoMapper.updateBadicInfo(userInfo);
        //如果是第一次保存,修改用户状态,添加一完成基本资料
        if(!(info.isBasicInfo())){
            Long state = BitStatesUtils.addState(info.getBitState(),BitStatesUtils.OP_BASIC_INFO);
            info.setBitState(state);
        }

        update(info);
    }

    /**
     *  定义一个方法,专门用来处理更新操作
     */
    public void update(UserInfo userInfo){
        int count = userInfoMapper.updateByPrimaryKey(userInfo);
        if(count == 0){
            throw new DisplayableException("用户信息修改出错,请联系客服,[乐观锁错误ID"+userInfo.getId()+"]");
        }
    }


}
