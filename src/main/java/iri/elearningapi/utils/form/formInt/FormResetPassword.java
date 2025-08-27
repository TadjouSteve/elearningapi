package iri.elearningapi.utils.form.formInt;

public class FormResetPassword {
	private String emailOrPhone;
	private String password;
	private String confirmPassword;
	private String codeChangePassword;

	public String getEmailOrPhone() {
		return emailOrPhone;
	}

	public void setEmailOrPhone(String emailOrPhone) {
		this.emailOrPhone = emailOrPhone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCodeChangePassword() {
		return codeChangePassword;
	}

	public void setCodeChangePassword(String codeChangePassword) {
		this.codeChangePassword = codeChangePassword;
	}

}
