package com.streenge.service.utils.formOut;

import java.util.List;

public class FormListProduits {
		private List<String> colunm=null;
		private List<RowTable> rows	= null;

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
}
