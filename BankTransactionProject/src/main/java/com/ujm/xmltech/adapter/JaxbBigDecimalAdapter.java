package com.ujm.xmltech.adapter;

import java.math.BigDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Class jaxb to adapt BigDecimal
 * 
 * @author UJM's students
 */
public class JaxbBigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public String marshal(BigDecimal obj) throws Exception {
        return obj.toString();
    }

    @Override
    public BigDecimal unmarshal(String obj) throws Exception {
        return new BigDecimal(obj.replaceAll(",", ""));
    }
}