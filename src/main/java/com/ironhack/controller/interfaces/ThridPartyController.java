package com.ironhack.controller.interfaces;

import com.ironhack.model.Utils.Transfer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface ThridPartyController {

    Transfer transfer(Authentication authentication, HttpServletRequest header, Transfer transfer);
}
