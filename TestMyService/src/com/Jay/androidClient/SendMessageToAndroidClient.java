package com.Jay.androidClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Jay.biz.StudentBiz;
import com.Jay.entity.Student;

public class SendMessageToAndroidClient extends HttpServlet {
	private StudentBiz studentBiz;
	
	public SendMessageToAndroidClient() {
		super();
		studentBiz = new StudentBiz();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		out.write(productJsonData());
		out.flush();
		out.close();
	}
	
	public String productJsonData(){
		List<Student> students = studentBiz.getJsonData();
		StringBuffer sb = new StringBuffer();
		
		sb.append("[");
		for(Student student : students){
			sb.append("{").append("'id':").append("'"+student.getId()+"'").append(",");
			sb.append("'name':").append("'"+student.getName()+"'").append(",");
			sb.append("'score':").append("'" + student.getScore() + "'");
			sb.append("}").append(",");
			System.out.println("111");
		}
		sb.deleteCharAt(sb.length()-1);
		//去掉最后一个逗号。
		sb.append("]");
		return new String(sb);
	}

}
