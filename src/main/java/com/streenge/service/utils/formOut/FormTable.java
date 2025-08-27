package com.streenge.service.utils.formOut;

import java.util.ArrayList;
import java.util.List;

public class FormTable {
	private List<String> colunm=null;
	private List<RowTable> rows	= new ArrayList<>();
	private int number=0;

	public List<String> getColunm() {
		return colunm;
	}

	public void setColunm(List<String> colunm) {
		this.colunm = colunm;
	}

	public List<RowTable> getRows() {
		return rows;
	}

	public void setRows(List<RowTable> rows) {
		this.rows = rows;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
