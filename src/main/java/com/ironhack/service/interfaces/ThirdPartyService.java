package com.ironhack.service.interfaces;

import com.ironhack.model.Utils.Transfer;
import jakarta.servlet.http.HttpServletRequest;

public interface ThirdPartyService {
    Transfer transfer(HttpServletRequest header, Transfer transfer);
}
