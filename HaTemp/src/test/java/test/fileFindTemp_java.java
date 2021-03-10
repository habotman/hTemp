package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class fileFindTemp_java  {
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("mainOut>>");
		System.out.println("**서버 서비스Id 추출");
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
				
				//TODO : 조건 넣어 java
				//if(true) {
				if(fList.get(i).getName().indexOf(".java") >= 0) {
					fileList.add(fList.get(i));
				}
			}
//        for(int i=0;i<fileList.size(); i++) {
//        		System.out.println(fileList.get(i));
//        }
			
			
			//"trans
			
			//검색 String
			String strR = "@ServiceId";
			String strR2 = "public";
			
			
			System.out.println("=========================="+strR+"==========================");
			System.out.println("====================================================Find Start");
			
			//파일 객체 생성
			File file1 = null;
			
			//정보객체
			List<String> fList1 = new ArrayList<String>();
			
			//스캐너로 파일 읽기
			Scanner scan1 = null;
			String input1 = "";
			String inputUpper = "";
			String strUpper = "";
			String addStr = "";
			int line = 0;
			for(int f=0; f<fileList.size(); f++) {
				
				//System.out.println(fileList[f].getName());
				//파일 객체 생성
				file1 = fileList.get(f);
				
				//스캐너로 파일 읽기
				scan1 = new Scanner(file1);
				input1 = "";
				inputUpper = "";
				strUpper = "";
				addStr = "";
				
				line = 0;
				while(scan1.hasNextLine()){
					line++;
					input1 = scan1.nextLine();
					
					if( input1.length() >= 0) {
						
						inputUpper = input1.toUpperCase();
						strUpper = strR.toUpperCase();
						if(inputUpper.indexOf( strUpper ) >= 0) {
							
							addStr = file1+""+ "	"+file1.getName()+""+ "	"+"line:"+line+"	" +""+"	";
							
							String gStr = "";
							String tStr = input1.toString().trim();
							
							//주석 걸러야지
							if(tStr.indexOf( "/" ) >= 0 && tStr.indexOf( "/" ) <2 ) {
								addStr +=	"주석";
							}
							if(tStr.indexOf( "*" ) >= 0 && tStr.indexOf( "*" ) <2 ) {
								addStr +=	"주석";
							}
							
							if(tStr.indexOf( strR ) >= 0) {
								for(int j=12; j<tStr.length(); j++) {
									
									//ID 추출
									if( "\"".equals(String.valueOf(tStr.charAt(j)) )) {
										break;
									}
									gStr += String.valueOf(tStr.charAt(j));
								}
							}
							addStr +=	"	" +gStr;
							addStr +=	"	" +(input1.toString().trim());
							
							//다음 행 진행 - 이후 아래에서 break
							while(scan1.hasNextLine()){
								
								input1 = scan1.nextLine();
								if(input1.indexOf( strR2 ) >= 0) {
									break;
								}// end if index
								
								addStr +=	"	" +(input1.toString().trim());
							}//end while
							
							//list add
							fList1.add( addStr );
						}// end if index

					}//end if length
					
				} //end while
				
			}//end for
			
			
			System.out.println("=================Find Count : " + fList1.size() + " 건");
			//log
			for(int i=0; i<fList1.size(); i++) {
				System.out.println(fList1.get(i));
			}
			
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