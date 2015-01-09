package com.shangbao.remotemodel;

import java.util.List;

public class ResponseModel {
	private int ResultCode;
	private String ResultMsg;
	private List<UserInfo> Data;
	public int getResultCode() {
		return ResultCode;
	}
	public void setResultCode(int resultCode) {
		ResultCode = resultCode;
	}
	public String getResultMsg() {
		return ResultMsg;
	}
	public void setResultMsg(String resultMsg) {
		ResultMsg = resultMsg;
	}
	public List<UserInfo> getData() {
		return Data;
	}
	public void setData(List<UserInfo> data) {
		Data = data;
	}
	
}
