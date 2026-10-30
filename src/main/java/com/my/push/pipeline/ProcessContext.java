package com.my.push.pipeline;

public class ProcessContext {

    private String code;
    private Object processModel;
    private Boolean needBreak;


    private Object response;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getProcessModel() {
        return processModel;
    }

    public void setProcessModel(Object processModel) {
        this.processModel = processModel;
    }
    public Boolean getNeedBreak() {
        return needBreak;
    }

    public void setNeedBreak(Boolean needBreak) {
        this.needBreak = needBreak;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }


}
