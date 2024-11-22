package com;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.used.model.UsedRepository;
import com.used.model.UsedVO;
import com.usedpic.model.UsedPicRepository;
import com.usedpic.model.UsedPicVO;

@SpringBootApplication
public class Test_Application_CommandLineRunner implements CommandLineRunner{
	
	@Autowired
	UsedPicRepository repository ;
	
	public static void main(String[] args) {
        SpringApplication.run(Test_Application_CommandLineRunner.class);
    }
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//● 新增

//				UsedVO usedVO = new UsedVO(); // 部門POJO
//				usedVO.setUsedNo(2);
//				File picFile = new File("C:/CIA103_WebAPP/eclipse_WTP_workspace4B_To-SpringBootMVC_1-5-8U/DoBuy/src/main/resources/static/images/husky-dog.gif");
//				FileInputStream fis = new FileInputStream(picFile);
//				byte[] pic = fis.readAllBytes();
//				UsedPicVO usedPicVO1 = new UsedPicVO();
//				usedPicVO1.setUsedPicName(picFile.getName());
//				usedPicVO1.setUsedPics(pic);
//				usedPicVO1.setUsedVO(usedVO);
//				repository.save(usedPicVO1);

				//● 修改
//				EmpVO empVO2 = new EmpVO();
//				empVO2.setEmpno(7001);
//				empVO2.setEname("吳永志2");
//				empVO2.setJob("MANAGER2");
//				empVO2.setHiredate(java.sql.Date.valueOf("2002-01-01"));
//				empVO2.setSal(new Double(20000));
//				empVO2.setComm(new Double(200));
//				empVO2.setDeptVO(deptVO);
//				repository.save(empVO2);
				
				//● 刪除   //●●● --> EmployeeRepository.java 內自訂的刪除方法
//				repository.deleteByEmpno(7014);
				
				//● 刪除   //XXX --> Repository內建的刪除方法目前無法使用，因為有@ManyToOne
				//System.out.println("--------------------------------");
				//repository.deleteById(7001);      
				//System.out.println("--------------------------------");

		    	//● 查詢-findByPrimaryKey (多方emp2.hbm.xml必須設為lazy="false")(優!)
//		    	Optional<EmpVO> optional = repository.findById(7001);
//				EmpVO empVO3 = optional.get();
//				System.out.print(empVO3.getEmpno() + ",");
//				System.out.print(empVO3.getEname() + ",");
//				System.out.print(empVO3.getJob() + ",");
//				System.out.print(empVO3.getHiredate() + ",");
//				System.out.print(empVO3.getSal() + ",");
//				System.out.print(empVO3.getComm() + ",");
//				// 注意以下三行的寫法 (優!)
//				System.out.print(empVO3.getDeptVO().getDeptno() + ",");
//				System.out.print(empVO3.getDeptVO().getDname() + ",");
//				System.out.print(empVO3.getDeptVO().getLoc());
//				System.out.println("\n---------------------");
		      
		    	
				//● 查詢-getAll (多方emp2.hbm.xml必須設為lazy="false")(優!)
//		    	List<EmpVO> list = repository.findAll();
//				for (EmpVO aEmp : list) {
//					System.out.print(aEmp.getEmpno() + ",");
//					System.out.print(aEmp.getEname() + ",");
//					System.out.print(aEmp.getJob() + ",");
//					System.out.print(aEmp.getHiredate() + ",");
//					System.out.print(aEmp.getSal() + ",");
//					System.out.print(aEmp.getComm() + ",");
//					// 注意以下三行的寫法 (優!)
//					System.out.print(aEmp.getDeptVO().getDeptno() + ",");
//					System.out.print(aEmp.getDeptVO().getDname() + ",");
//					System.out.print(aEmp.getDeptVO().getLoc());
//					System.out.println();
//				}
	}
}
