package app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String bio;
    private String email;
    private String username;
    private String password;
    private String verificationKey;
    private Boolean isVerified;
    private String resetToken;
    private byte[] picture;
    private String picture64;
    private String role;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return verificationKey;
    }

    public void setKey(String key) {
        this.verificationKey = key;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean status) {
        this.isVerified = status;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String token) {
        this.resetToken = token;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPicture64() {
        return picture64;
    }

    public void setPicture64(String picture64) {
        this.picture64 = picture64;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}