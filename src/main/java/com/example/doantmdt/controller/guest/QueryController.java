package com.example.doantmdt.controller.guest;

import com.example.doantmdt.payload.ResponseBody;
import com.example.doantmdt.service.implement.IQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {

    @Autowired
    private IQueryService iQueryService;

    @GetMapping("/api/guest/base/create")
    public ResponseEntity<ResponseBody> createRootUsers(){
        boolean isSuccess = false;
        if(iQueryService.createRootRoles()){
            if(iQueryService.createRootAccount()){
                isSuccess = true;
            }
        }
        if(isSuccess){
            return new ResponseEntity<>(new ResponseBody(true, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(new ResponseBody(false, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS), HttpStatus.OK);
        }
    }
}
