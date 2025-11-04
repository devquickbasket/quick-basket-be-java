package com.quickbasket.quickbasket.address;

import com.quickbasket.quickbasket.user.User;
import com.quickbasket.quickbasket.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private UserRepository userRepository;

    public List<UserAddressResponse> userAddress(String userId) {
        List<UserAddress> userAddresses = userAddressRepository.findByUser_Id(userId);

        List<UserAddressResponse> userAddressResponses = userAddresses.stream()
                .map(UserAddressResponse::new) // map each UserAddress to UserAddressResponse
                .toList();

        return userAddressResponses;
    }

    public UserAddressResponse storeAddress(String userId, AddUserAddressRequest userAddressRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        UserAddress userAddress = new UserAddress();

        userAddress.setAddress(userAddressRequest.getAddress());
        userAddress.setCity(userAddressRequest.getCity());
        userAddress.setState(userAddressRequest.getState());
        userAddress.setLatitude(userAddressRequest.getLatitude());
        userAddress.setLongitude(userAddressRequest.getLongitude());
        userAddress.setStreet(userAddressRequest.getStreet());

        userAddress.setUser(user);

        UserAddress savedAddress = userAddressRepository.save(userAddress);

        return new UserAddressResponse(savedAddress);
    }

    public UserAddressResponse changeStatus(String userId, String addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        userAddressRepository.changeAllUserAddressStatus(userId,100);

        userAddress.setStatus(200);


        UserAddress savedAddress = userAddressRepository.save(userAddress);
        return new UserAddressResponse(savedAddress);


    }
}
