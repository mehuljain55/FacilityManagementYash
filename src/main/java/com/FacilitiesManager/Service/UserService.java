package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Enums.UserApprovalStatus;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.User;
import com.FacilitiesManager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public ApiResponseModel getUserApprovalList(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent()) {
            User user=opt.get();
            List<User> users = userRepo.findUserPendingRequest(user.getOfficeId(), UserApprovalStatus.PENDING);
            if (users != null && users.size()>0) {
                return new ApiResponseModel<>(StatusResponse.success, users, "User Approval List");
            }else {
                return new ApiResponseModel<>(StatusResponse.not_found,null,"No approval pending");
            }
        }else {
            return new ApiResponseModel<>(StatusResponse.unauthorized,null,"Unauthorized user");
        }
    }

    public ApiResponseModel approveUser(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent()) {
            User user=opt.get();
            user.setStatus(UserApprovalStatus.ACTIVE);
            userRepo.save(user);
            return new ApiResponseModel<>(StatusResponse.success,null,"User approved");
        }else {
            return new ApiResponseModel<>(StatusResponse.unauthorized,null,"Unauthorized user");
        }
    }

    public ApiResponseModel userApprovalCancel(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent()) {
            User user=opt.get();
            user.setStatus(UserApprovalStatus.BLOCKED);
            userRepo.save(user);
            return new ApiResponseModel<>(StatusResponse.success,null,"User blocked");
        }else {
            return new ApiResponseModel<>(StatusResponse.unauthorized,null,"Unauthorized user");
        }
    }



}
