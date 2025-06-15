package com.finance.sugarmarket.auth.cache;

import com.finance.sugarmarket.auth.dto.UserDetailsDTO;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.model.MapRoleUser;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.auth.repo.MapRoleUserRepo;
import com.finance.sugarmarket.base.cache.AbstractCacheProvider;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.RedisConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCacheProvider extends AbstractCacheProvider<UserDetailsDTO> {

    @Autowired
    private MFUserRepo userRepo;
    @Autowired
    private MapRoleUserRepo mapRoleUserRepo;

    @Autowired
    private ModelMapper modelMapper;

    public UserCacheProvider() {
        super(UserDetailsDTO.class); // Provide the type token for generic casting
    }

    @Override
    public String getFinalKey(String key) {
        return RedisConstants.USER_DETAIL + AppConstants.HASH_DELIMITER + key;
    }

    public UserDetailsDTO getUserDetails(Long userId) {
        String key = getFinalKey(String.valueOf(userId));
        UserDetailsDTO cachedUser = get(key);
        if (cachedUser != null) {
            return cachedUser;
        }

        MFUser user = userRepo.findById(userId).orElse(null);
        MapRoleUser mapRoleUser = mapRoleUserRepo.findByUser(userId);
        UserDetailsDTO dto = modelMapper.map(user, UserDetailsDTO.class);
        dto.setFullName(user.getFullname());
        dto.setPhoneNumber(user.getPhonenumber());
        dto.setEnabled(true);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(mapRoleUser.getRole().getRoleName()));
        dto.setAuthorities(authorities);

        put(key, dto);
        return dto;
    }

    public void delete(Long userId) {
        super.remove(getFinalKey(String.valueOf(userId)));
    }
}
