package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Enums.UserApprovalStatus;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.Model.UserLoginModel;
import com.FacilitiesManager.Entity.User;
import com.FacilitiesManager.Repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAuthorizationService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private  MailingService mailingService;


    public ApiResponseModel<UserLoginModel> registerUser(User userRequest) throws MessagingException {
        Optional<User> opt=userRepo.findById(userRequest.getEmailId());
        if(opt.isPresent())
        {
            return  new ApiResponseModel<>(StatusResponse.failed,null,"User already exists");
        }else {
            userRequest.setRole(AccessRole.user);
            userRequest.setStatus(UserApprovalStatus.PENDING);
            User user =userRepo.save(userRequest);
            String content=mailingService.userRequest(user);
            List<String> managers=userRepo.findEmailsByRoleAndOfficeId(AccessRole.manager, user.getOfficeId());
            mailingService.sendMail(managers,"User account approval notification",content, user.getEmailId());

            return  new ApiResponseModel<>(StatusResponse.success,null,"User added");
        }
    }


    public ApiResponseModel<UserLoginModel> validateUserLogin(String userId,String password)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent())
        {
            User user=opt.get();
            if(user.getPassword().equals(password)&& user.getStatus().equals(UserApprovalStatus.ACTIVE))
            {
                String token=jwtUtils.generateToken(user);
                UserLoginModel userLoginModel=new UserLoginModel(user,token);
                return  new ApiResponseModel<>(StatusResponse.success,userLoginModel,"User Validated");
            }else {
                return  new ApiResponseModel<>(StatusResponse.unauthorized,null,"Invalid Credential");
            }
        }else {
            return  new ApiResponseModel<>(StatusResponse.not_found,null,"User Not exists");
        }
    }


    public boolean validateUserToken(String userId,String token)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent())
        {
            User user=opt.get();
            boolean status=jwtUtils.validateTokenForUser(user,token);
            return  status;
        } else {
            return  false;
        }
    }

    public boolean validateUserAccess(User userModel, String token, AccessRole requestedAccessRole)
    {
        Optional<User> opt=userRepo.findById(userModel.getEmailId());
        if(opt.isPresent())
        {
            User user=opt.get();
            boolean status=jwtUtils.validateTokenForUserRole(user,token,requestedAccessRole);
            return  status;
        } else {
            return  false;
        }
    }


    }




