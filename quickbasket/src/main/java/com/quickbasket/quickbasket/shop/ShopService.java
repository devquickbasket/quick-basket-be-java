package com.quickbasket.quickbasket.shop;

import com.quickbasket.quickbasket.shop.requests.AssignAgentRequest;
import com.quickbasket.quickbasket.shop.requests.CreateShopRequest;
import com.quickbasket.quickbasket.shop.response.ShopAgentResponse;
import com.quickbasket.quickbasket.shop.response.ShopResponse;
import com.quickbasket.quickbasket.user.User;
import com.quickbasket.quickbasket.user.UserRepository;
import com.quickbasket.quickbasket.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public Page<ShopResponse> index(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ShopResponse> shopPage = shopRepository.findAll(pageable).map(ShopResponse::new);

        return shopPage;
    }

    public List<ShopResponse> shopAll() {

        return shopRepository.findByStatus(200)
                .stream()
                .map(ShopResponse::new)
                .collect(Collectors.toList());
    }



    public ShopResponse create(CreateShopRequest request) {

        Shop shop = new Shop(request);

        shop.setStatus(200);

        shopRepository.save(shop);

        return new ShopResponse(shop);
    }

    public ShopResponse show(String shopId) {

        Shop shop = shopRepository.findById(shopId).orElse(null);

        if (shop == null) {
            throw new RuntimeException("Shop with id " + shopId + " not found");
        }

        return new ShopResponse(shop);
    }

    public ShopResponse assignAgent(AssignAgentRequest request) {
        Shop shop = shopRepository.findById(request.getShopId()).orElse(null);

        if (shop == null) {
            throw new RuntimeException("Shop with id " + request.getShopId() + " not found");
        }

        User user = userRepository.findAgentById(request.getAgentId()).orElse(null);

        if (user == null) {
            throw new RuntimeException("Agent with id " + request.getAgentId() + " not found");
        }

        List<User> agents = new ArrayList<>();

        agents.add(user);

        shop.setAgents(agents);

        shopRepository.save(shop);

        return new ShopResponse(shop);
    }

    public List<ShopAgentResponse> shopAgents(String shopId){
        Shop shop = shopRepository.findById(shopId).orElse(null);
        if (shop == null) {
            throw new RuntimeException("Shop with id " + shopId + " not found");
        }
        List<ShopAgentResponse> agents = shop.getAgents() != null ?
                shop.getAgents().stream().map(ShopAgentResponse::new).collect(Collectors.toList()) :
                new ArrayList<>();

        return agents;
    }

    public ShopResponse update(String shopId, CreateShopRequest request) {
        Shop shop = shopRepository.findById(shopId).orElse(null);

        if (shop == null) {
            throw new RuntimeException("Shop with id " + shopId + " not found");
        }

        shop.setName(request.getName());
        shop.setAddress(request.getAddress());
        shop.setEmail(request.getEmail());
        shop.setLatitude(request.getLatitude());
        shop.setLongitude(request.getLongitude());
        shop.setSlug(request.getSlug());
        shop.setState(request.getState());
        shop.setStatus(request.getStatus());

        shopRepository.save(shop);
        return new ShopResponse(shop);
    }
}
