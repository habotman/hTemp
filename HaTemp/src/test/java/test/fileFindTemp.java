package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class fileFindTemp  {
	public static void main(String[] args) {
		
		System.out.println("mainOut>>");
		System.out.println("JAVA Execute Completed");
		
		try{
			
			System.out.println("====================================================Find Start");
			
			//폴더
			File path =   new File("D:\\02. Framework\\lse\\");
			File[] fileList = path.listFiles();
			
			for(int f=0; f<fileList.length; f++) {
				
//				if("poplist".equals(fileList[f].getName())) {
//					continue;
//				}
				//System.out.println(fileList[f].getName());
				//파일 객체 생성
				File file1 = fileList[f];
				
				List<String> fList1 = new ArrayList<String>();
				
				//스캐너로 파일 읽기
				Scanner scan1 = new Scanner(file1);
				String input1 = "";
				String inputUpper = "";
				String strUpper = "";
				
				int line = 0;
				while(scan1.hasNextLine()){
					line++;
					input1 = scan1.nextLine();
					
					if( input1.length() >= 0) {
						
						inputUpper = input1.toUpperCase();
						strUpper = "popup:true".toUpperCase();
						if(inputUpper.indexOf( strUpper ) >= 0) {
							fList1.add(fileList[f].getName()+":"+ "	"+"line:"+line+"	" +""+"	"+""+ "	" +(input1.toString().trim()) );
						}
					}
					
//					if( input1.length() >= 0) {
//						for(int i=0; i<fList.size(); i++) {
//							if(input1.indexOf(fList.get(i)) >= 0) {
//								fList1.add(fileList[f].getName()+":"+ "	"+"line:"+line+"	" +fList.get(i)+"	"+""+ "	" +(input1.toString().trim()) );
//							}
//						}//end for
//					}
					
				} //end while
				
				for(int i=0; i<fList1.size(); i++) {
					System.out.println(fList1.get(i));
				}
				
			}//end for
			
			System.out.println("====================================================Find End");
			
			//System.out.println(scan.useDelimiter("\\z").next());
		}catch (FileNotFoundException e) {
			// TODO: handle exception
		}
		
		
		
		
	}    
  }