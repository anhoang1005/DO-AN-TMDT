package com.example.doantmdt.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringToJsonExample {
    public static void main(String[] args) {
        // Chuỗi JSON
        String jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + "Explain how works" + "\"}]}]}";

        try {
            // Tạo ObjectMapper từ Jackson
            ObjectMapper objectMapper = new ObjectMapper();

            // Chuyển chuỗi thành JsonNode (đối tượng JSON)
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // In ra đối tượng JSON
            System.out.println(jsonNode.toPrettyString()); // Hiển thị JSON đẹp mắt
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

