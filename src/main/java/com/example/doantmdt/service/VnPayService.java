package com.example.doantmdt.service;

import com.example.doantmdt.payload.ResponseBody;
import java.io.UnsupportedEncodingException;

public interface VnPayService {

	ResponseBody createPayment(long total, String orderType) throws UnsupportedEncodingException;

}
