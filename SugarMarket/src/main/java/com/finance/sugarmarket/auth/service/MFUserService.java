package com.finance.sugarmarket.auth.service;

import com.finance.sugarmarket.auth.dto.UserDetailsDTO;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MFUserService {

    @Autowired
    private MFUserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;

    public MFUser getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<UserDetailsDTO> findAllUserDTOs() {
        List<MFUser> users = userRepo.findAll();
        return users.stream()
                .map(user -> {
                    UserDetailsDTO userDetailDto = modelMapper.map(user, UserDetailsDTO.class);
                    userDetailDto.setFullName(user.getFullname());
                    userDetailDto.setEnabled(user.getIsActive());
                    return userDetailDto;
                })
                .collect(Collectors.toList());
    }

    public List<UserDetailsDTO> findAllActiveUserDTOs() {
        List<MFUser> activeUsers = userRepo.findActiveUserList();
        return activeUsers.stream()
                .map(user -> {
                    UserDetailsDTO userDetailDto = modelMapper.map(user, UserDetailsDTO.class);
                    userDetailDto.setFullName(user.getFullname());
                    userDetailDto.setEnabled(user.getIsActive());
                    return userDetailDto;
                })
                .collect(Collectors.toList());
    }

    public MFUser findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }
}
