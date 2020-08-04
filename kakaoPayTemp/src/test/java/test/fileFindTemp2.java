package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class fileFindTemp2  {
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("mainOut>>");
		System.out.println("JAVA Execute Completed");
		
		try {
			//폴더
			// 읽어올 경로
			File dir =new File("D:\\02. Framework\\trunk\\");
			
			// 탐색할 파일을 저장할 리스트
			List<File> fList = new ArrayList<File>();
			List<File> fileList = new ArrayList<File>();
			
			// 파일을 탐색한다.
			fList =  dirAllFiles(fList, dir);
			
			
			// 탐색한 파일을 하나씩 출력할 파일에 쓴다.
			// .java 만
			for(int i=0;i<fList.size(); i++) {
				if(fList.get(i).getName().indexOf(".java") >= 0) {
					fileList.add(fList.get(i));
				}
			}
//        for(int i=0;i<fileList.size(); i++) {
//        		System.out.println(fileList.get(i));
//        }
			
			//검색 String
			String strR = "wordnam";
			
			
			System.out.println("=========================="+strR+"==========================");
			System.out.println("====================================================Find Start");
			
			for(int f=0; f<fileList.size(); f++) {
				
				//System.out.println(fileList[f].getName());
				//파일 객체 생성
				File file1 = fileList.get(f);
				
				List<String> fList1 = new ArrayList<String>();
				
				//스캐너로 파일 읽기
				Scanner scan1 = new Scanner(file1);
				String input1 = "";
				String inputUpper = "";
				String strUpper = "";
				String addStr = "";
				
				int line = 0;
				while(scan1.hasNextLine()){
					line++;
					input1 = scan1.nextLine();
					
					if( input1.length() >= 0) {
						
						inputUpper = input1.toUpperCase();
						strUpper = strR.toUpperCase();
						if(inputUpper.indexOf( strUpper ) >= 0) {
							
							addStr = file1+":"+ "	"+file1.getName()+":"+ "	"+"line:"+line+"	" +""+"	"+""+ "	" +(input1.toString().trim());
							if(input1.indexOf( ".vo." ) >= 0) {
								addStr += "	" + "vo";
							}
							
							fList1.add(addStr );
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
	
	/**
	 * 
	 * @param fList
	 * @param dir
	 * @return
	 */
    private static List<File> dirAllFiles(List<File> fList, File dir) {
 
        if(dir.isDirectory()) {
            File[] children = dir.listFiles();
            for(File f : children) {
                // 재귀 호출 사용
                // 하위 폴더 탐색 부분
                dirAllFiles(fList,f);
            }
        } else {
            fList.add(dir);
        }
		return fList;
    }
	
  }