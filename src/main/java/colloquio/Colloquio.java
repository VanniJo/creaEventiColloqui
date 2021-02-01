package colloquio;

import java.util.Date;

public class Colloquio {
	private String alunno;
	private Date dataOra;
	
	public String getAlunno() {
		return alunno;
	}

	public void setAlunno(String alunno) {
		this.alunno = alunno;
	}

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	@Override
	public String toString() {
		return "Colloquio [Alunno=" + this.alunno + ", dataOra=" + dataOra + "]";
	}
}
