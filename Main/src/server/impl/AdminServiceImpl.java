package server.impl;

import server.context.ServiceContext;
import server.domain.User;
import server.service.AdminService;

import java.util.List;
import java.util.Optional;

//impl package should be inside the service package
public class AdminServiceImpl implements AdminService {

    //user service method
    private boolean isUsernameAvailable(String username) {
        List<User> userList = ServiceContext.getUserService().getUsers();
        for (User user : userList) {
            String existingUsername = user.getUsername();
            if (existingUsername.equals(username)) {
                return false; // Username already exists
            }
        }
        return true; // Username is available
    }

    public boolean addUser(User user) {
        if (isUsernameAvailable(user.getUsername())) {

            List<User> userList = ServiceContext.getUserService().getUsers();
            userList.add(user);
            ServiceContext.getUserService().saveUsersToDb(userList);
            return true;

        } else {
            System.out.println("Error: Username '" + user.getUsername() + "' already exists. Please choose a different username.");
            return false;
        }
    }


    public void setUserAccountStatus(String username,boolean status) {

        //you may add a method in user service like getUserbyName
        Optional<User> targetUser = ServiceContext.getUserService().getUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        if(targetUser.isPresent())
        {
            User user = targetUser.get();
            System.out.println(user);
            //code to update the current user in list and then store the complete list on disk
            List<User> userList = ServiceContext.getUserService().getUsers();
            int targetIndex = userList.indexOf(user);
            System.out.println(targetIndex);

            userList.remove(targetIndex);

            //update the user account status
            user.setAccountStatus(status);

            System.out.println("--");

//             add the updated
            userList.add(targetIndex,targetUser.get());

            ServiceContext.getUserService().saveUsersToDb(userList);


            return;


        }

        System.out.printf("Error: No server.domain.User found against this the provided '%s' username.\n",username);

    }


    //no need of main method
    public static void main(String[] args){
//        UserService.viewPatients();
//
//        AdminService.addUser(UserService.getUsers().get(0));
//
//        AdminService.setUserAccountStatus("admin123000",true);
//        AdminService.setUserAccountStatus("admin123",true);
    }

}
