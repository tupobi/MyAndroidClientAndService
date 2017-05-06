package com.Jay.androidClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class MultiFileUpload extends HttpServlet {

	public MultiFileUpload() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		// Ҫִ���ļ��ϴ��Ĳ���
		// �жϱ��Ƿ�֧���ļ��ϴ�������enctype="multipart/form-data"
		boolean isMultipartContent = ServletFileUpload
				.isMultipartContent(request);
		if (!isMultipartContent) {
			System.out.println("your form is not multipart/form-data");
		}

		// ����һ��DiskFileItemfactory������
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File("f:\\"));// ָ����ʱ�ļ��Ĵ洢Ŀ¼
		// ����һ��ServletFileUpload���Ķ���
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// ����ϴ�������������
		sfu.setHeaderEncoding("UTF-8");
		
		// ����request���󣬲��õ�һ������ļ���
		try {
			// �����ϴ��ļ��Ĵ�С
			 sfu.setFileSizeMax(1024*1024*10);//��ʾ10M��С
			 sfu.setSizeMax(1024*1024*10);
			List<FileItem> fileItems = sfu.parseRequest(request);

			// ������������
			for (FileItem fileitem : fileItems) {
				if (fileitem.isFormField()) {
					// ��ͨ����
					processFormField(fileitem);
				} else {
					// �ϴ�����
					processUploadField(fileitem);
				}
			}

		} catch (FileUploadBase.FileSizeLimitExceededException e) {
			// throw new RuntimeException("�ļ����ڣ����ܳ���3M");

			System.out.println("�ļ����ڣ����ܳ���3M");
		} catch (FileUploadBase.SizeLimitExceededException e) {
			System.out.println("���ļ���С���ܳ���6M");
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		out.flush();
		out.close();
	}
	
	private void processUploadField(FileItem fileitem) {
		try {
			// �õ��ļ�������
			InputStream is = fileitem.getInputStream();
			System.out.println("�ļ��ֶ�����:" + fileitem.getFieldName());
			// ����һ���ļ����̵�Ŀ¼
			String directoryRealPath = this.getServletContext().getRealPath(
					"/WEB-INF/upload");
			File storeDirectory = new File(directoryRealPath);// �ȴ����ļ��ִ���Ŀ¼
			if (!storeDirectory.exists()) {
				storeDirectory.mkdirs();// ����һ��ָ����Ŀ¼
			}
			// �õ��ϴ�������
			String filename = fileitem.getName();// �ļ����е�ֵ F:\ͼƬ�ز�\С����\43.jpg ����
													// 43.jpg
			if (filename != null) {
				// filename =
				// filename.substring(filename.lastIndexOf(File.separator)+1);
				filename = FilenameUtils.getName(filename);// Ч��ͬ��
			}

			// ����ļ�ͬ��������
			filename = UUID.randomUUID() + "_" + filename;
			// �����ڴ�ɢĿ¼
			// String childDirectory = makeChildDirectory(storeDirectory); //
			// 2015-10-19
			String childDirectory = makeChildDirectory(storeDirectory, filename); // a/b

			// �ϴ��ļ�
			fileitem.write(new File(storeDirectory, childDirectory
					+ File.separator + filename));
			fileitem.delete();
			//ɾ����ʱ�ļ�
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//������ͨ����
	private void processFormField(FileItem fileitem) {
		try {
			String fieldname = fileitem.getFieldName();// �ֶ���
			String fieldvalue = fileitem.getString("UTF-8");// �ֶ�ֵ
			//fieldvalue = new String(fieldvalue.getBytes("iso-8859-1"),"utf-8");
			System.out.println(fieldname + "=" + fieldvalue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	// �����ڴ�ɢ
	/*
	 * private String makeChildDirectory(File storeDirectory) {
	 * 
	 * SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd"); String
	 * dateDirectory = sdf.format(new Date()); //ֻ�ܴ���Ŀ¼ File file = new
	 * File(storeDirectory,dateDirectory); if(!file.exists()){ file.mkdirs(); }
	 * 
	 * return dateDirectory; }
	 */
	//��Ŀ¼��ɢ
	private String makeChildDirectory(File storeDirectory, String filename) {
		int hashcode = filename.hashCode();// �����ַ�ת����32λhashcode��
//		System.out.println(hashcode);
		String code = Integer.toHexString(hashcode); // ��hashcodeת��Ϊ16���Ƶ��ַ�
														// abdsaf2131safsd
//		System.out.println(code);
		String childDirectory = code.charAt(0) + File.separator
				+ code.charAt(1); // a/b
		// ����ָ��Ŀ¼
		File file = new File(storeDirectory, childDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
		return childDirectory;
	}
}
