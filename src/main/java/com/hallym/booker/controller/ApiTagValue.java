package com.hallym.booker.controller;

import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Component
public class ApiTagValue {

    // tag값의 정보를 가져오는 함수
    public String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        return nlList.item(0).getTextContent();
    }
}

