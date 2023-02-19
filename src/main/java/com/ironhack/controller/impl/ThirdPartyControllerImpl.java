package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.ThridPartyController;
import com.ironhack.model.Utils.Transfer;
import com.ironhack.service.interfaces.ThirdPartyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ThirdPartyControllerImpl implements ThridPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    /* POSTMAPPING Transfer - ThirdParty */
    @PostMapping("User/accounts/thirdparty/transfer/")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer transfer(Authentication authentication, @RequestBody @Valid HttpServletRequest header, Transfer transfer) {
        return thirdPartyService.transfer(header, transfer);
    }

}

