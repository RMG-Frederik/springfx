package net.codecrafting.springfx.demo.form;

import java.time.LocalDate;

import org.hibernate.validator.constraints.NotEmpty;

import javafx.scene.paint.Color;
import net.codecrafting.springfx.annotation.ValidationBind;
import net.codecrafting.springfx.context.ViewContext;

public class DemoForm extends JFXFormModel
{
	@NotEmpty(message="User field is required")
	@ValidationBind("userField")
	private String user;
	
	@NotEmpty(message="Pass field is required")
	@ValidationBind("passField")
	private String pass;
	
	@NotEmpty(message="Text field is required")
	@ValidationBind("textField")
	private String text;
	
	@ValidationBind("datePicker")
	private LocalDate date;
	
	@ValidationBind("colorPicker")
	private Color color;
	
	private boolean funToggle;
	
	public DemoForm(ViewContext context) 
	{
		super(context);
	}
	
	public String getUser() 
	{
		return user;
	}
	
	public void setUser(String user) 
	{
		this.user = user.toUpperCase();
	}
	
	public String getPass() 
	{
		return pass;
	}
	
	public void setPass(String pass) 
	{
		this.pass = pass;
	}

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}

	public LocalDate getDate() 
	{
		return date;
	}

	public void setDate(LocalDate date) 
	{
		this.date = date;
	}

	public Color getColor() 
	{
		return color;
	}

	public void setColor(Color color) 
	{
		this.color = color;
	}
	
	public boolean isFunToggle() 
	{
		return funToggle;
	}

	public void setFunToggle(boolean funToggle) 
	{
		this.funToggle = funToggle;
	}

	@Override
	protected void postUpdateValues() 
	{
		user = user.toUpperCase();
	}

	@Override
	protected void preUpdateValues() 
	{
		user = user.toUpperCase();
	}

	@Override
	public String toString() 
	{
		return "DemoForm [user=" + user + ", pass=" + pass + ", text=" + text + ", date=" + date + ", color=" + color
				+ ", funToggle=" + funToggle + "]";
	}
}
