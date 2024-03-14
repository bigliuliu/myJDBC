package com.liu.learnjava.entity;

import lombok.Data;

@Data
public class ResultResponse {
	// public ResultResponse() {
	//
	// }
	// public ResultResponse(Record record) {
	// 	this.record = record;
	// }

	// public Record getRecord() {
	// 	return record;
	// }
	//
	// public void setRecord(Record record) {
	// 	this.record = record;
	// }

	private Record record;
	@Data
	public static class Record{
		// public Record() {
		//
		// }
		// public Record(Object data, String code) {
		// 	this.data = data;
		// 	this.code = code;
		// }
		//
		// public Object getData() {
		// 	return data;
		// }
		//
		// public void setData(Object data) {
		// 	this.data = data;
		// }
		//
		// public String getCode() {
		// 	return code;
		// }
		//
		// public void setCode(String code) {
		// 	this.code = code;
		// }

		private Object data;
		private String code;
	}
}