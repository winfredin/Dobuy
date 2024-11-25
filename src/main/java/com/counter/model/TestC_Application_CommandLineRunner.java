package com.counter.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.persistence.ManyToOne;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.counter.model.CounterRepository;
import com.counter.model.CounterVO;
import com.counterType.model.CounterTypeRepository;
import com.counterType.model.CounterTypeVO;







@SpringBootApplication
public class TestC_Application_CommandLineRunner implements CommandLineRunner {
    
	@Autowired
	CounterRepository repository ;
	
	@Autowired
	CounterTypeRepository repository2 ;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public static void main(String[] args) {
        SpringApplication.run(TestC_Application_CommandLineRunner.class);
    }

    @Override
    public void run(String...args) throws Exception {

    	//● 新增


//        CounterVO counterVO1 = new CounterVO();
//        counterVO1.setCounterAccount("usertest");
//        counterVO1.setCounterName("tttttttt");
//        counterVO1.setCounterPassword("12345");
//        counterVO1.setCounterAddress("123 Street Name");
//        counterVO1.setCounterPhone("0912345678");
//        counterVO1.setCounterUid("A123456789");
//        counterVO1.setCounterEmail("example1@example.com");
//        counterVO1.setCounterUbn("24689743");
//        counterVO1.setCounterCName("TESTCOUNTER");
//        counterVO1.setCounterTypeNo(3);
//        counterVO1.setCounterInform("Counter information");
//        counterVO1.setCounterStatus(1);
//        repository.save(counterVO1);

    	
//    	CounterTypeVO counterTypeVO1 = new CounterTypeVO();
//    	counterTypeVO1.setCounterTName("中華隊");
//    	 repository2.save(counterTypeVO1);

		//● 修改
//		 CounterVO counterVO2 = new CounterVO();
//		counterVO2.setCounterNo(10);
//        counterVO2.setCounterAccount("usertest");
//        counterVO2.setCounterName("我不知道1");
//        counterVO2.setCounterPassword("12345");
//        counterVO2.setCounterAddress("123 Street Name");
//        counterVO2.setCounterPhone("0912345678");
//        counterVO2.setCounterUid("A123456789");
//        counterVO2.setCounterEmail("example1@example.com");
//        counterVO2.setCounterUbn("24689743");
//        counterVO2.setCounterCName("TESTCOUNTER1");
//        counterVO2.setCounterTypeNo(2);
//        counterVO2.setCounterInform("我還在測試");
//        counterVO2.setCounterStatus(0);
//        repository.save(counterVO2);
    	
//    	CounterTypeVO counterTypeVO2 = new CounterTypeVO();
//    	counterTypeVO2.setCounterTypeNo(8);
//    	counterTypeVO2.setCounterTName("日本隊");
//    	 repository2.save(counterTypeVO2);
		
		//● 刪除   //●●● --> EmployeeRepository.java 內自訂的刪除方法
//		repository.deleteByEmpno(7014);
		
		//● 刪除   //XXX --> Repository內建的刪除方法目前無法使用，因為有@ManyToOne
		//System.out.println("--------------------------------");
		//repository.deleteById(7001);      
		//System.out.println("--------------------------------");

    	//● 查詢-findByPrimaryKey (多方emp2.hbm.xml必須設為lazy="false")(優!)
//    	Optional<EmpVO> optional = repository.findById(7001);
//		EmpVO empVO3 = optional.get();
//		System.out.print(empVO3.getEmpno() + ",");
//		System.out.print(empVO3.getEname() + ",");
//		System.out.print(empVO3.getJob() + ",");
//		System.out.print(empVO3.getHiredate() + ",");
//		System.out.print(empVO3.getSal() + ",");
//		System.out.print(empVO3.getComm() + ",");
//		// 注意以下三行的寫法 (優!)
//		System.out.print(empVO3.getDeptVO().getDeptno() + ",");
//		System.out.print(empVO3.getDeptVO().getDname() + ",");
//		System.out.print(empVO3.getDeptVO().getLoc());
//		System.out.println("\n---------------------");
      
    	
		//● 查詢-getAll (多方emp2.hbm.xml必須設為lazy="false")(優!)
//      List<CounterVO> counters = repository.findAll();
//      for (CounterVO counter : counters) {
//          System.out.println("No: " + counter.getCounterNo());
//          System.out.println("Account: " + counter.getCounterAccount());
//          System.out.println("Name: " + counter.getCounterName());
//          System.out.println("Password: " + counter.getCounterPassword());
//          System.out.println("Address: " + counter.getCounterAddress());
//          System.out.println("Phone: " + counter.getCounterPhone());
//          System.out.println("Uid: " + counter.getCounterUid());
//          System.out.println("Email: " + counter.getCounterEmail());
//          System.out.println("Ubn: " + counter.getCounterUbn());
//          System.out.println("CName: " + counter.getCounterCName());
//          System.out.println("Email: " + counter.getCounterEmail());
//          System.out.println("Type: " + counter.getCounterTypeVO().getCounterTName());
//          System.out.println("INF: " + counter.getCounterInform());
//          System.out.println("Status: " + counter.getCounterStatus());
//          System.out.println("-------------------------");
//      }

		//● 複合查詢-getAll(map) 配合 req.getParameterMap()方法 回傳 java.util.Map<java.lang.String,java.lang.String[]> 之測試
//		Map<String, String[]> map = new TreeMap<String, String[]>();
//		map.put("empno", new String[] { "7001" });
//		map.put("ename", new String[] { "KING" });
//		map.put("job", new String[] { "PRESIDENT" });
//		map.put("hiredate", new String[] { "1981-11-17" });
//		map.put("sal", new String[] { "5000.5" });
//		map.put("comm", new String[] { "0.0" });
//		map.put("deptno", new String[] { "1" });
//		
//		List<EmpVO> list2 = HibernateUtil_CompositeQuery_Emp3.getAllC(map,sessionFactory.openSession());
//		for (EmpVO aEmp : list2) {
//			System.out.print(aEmp.getEmpno() + ",");
//			System.out.print(aEmp.getEname() + ",");
//			System.out.print(aEmp.getJob() + ",");
//			System.out.print(aEmp.getHiredate() + ",");
//			System.out.print(aEmp.getSal() + ",");
//			System.out.print(aEmp.getComm() + ",");
//			// 注意以下三行的寫法 (優!)
//			System.out.print(aEmp.getDeptVO().getDeptno() + ",");
//			System.out.print(aEmp.getDeptVO().getDname() + ",");
//			System.out.print(aEmp.getDeptVO().getLoc());
//			System.out.println();
//		}
		

		//● (自訂)條件查詢
//		List<EmpVO> list3 = repository.findByOthers(7001,"%K%",java.sql.Date.valueOf("1981-11-17"));
//		if(!list3.isEmpty()) {
//			for (EmpVO aEmp : list3) {
//				System.out.print(aEmp.getEmpno() + ",");
//				System.out.print(aEmp.getEname() + ",");
//				System.out.print(aEmp.getJob() + ",");
//				System.out.print(aEmp.getHiredate() + ",");
//				System.out.print(aEmp.getSal() + ",");
//				System.out.print(aEmp.getComm() + ",");
//				// 注意以下三行的寫法 (優!)
//				System.out.print(aEmp.getDeptVO().getDeptno() + ",");
//				System.out.print(aEmp.getDeptVO().getDname() + ",");
//				System.out.print(aEmp.getDeptVO().getLoc());
//				System.out.println();
//			}
//		} else System.out.print("查無資料\n");
//		System.out.println("--------------------------------");

    }
}
