import java.util.*;
import java.io.*;

public class Main {
	//동굴 위치 관련 클래스
    static class position{
        int x, y;
        public position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static int R,C,N;
    static char[][] cave;	//동굴 정보 저장 배열
    static int[] H;		//막대기 높이 저장 배열
    static int[] dx = {0, -1, 1, 0};	//하좌우상 x값 변경
    static int[] dy = {-1, 0, 0, 1};	//하좌우상 y값 변경
    static public void  main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //입력값 처리하는 BufferedReader
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        //결과값 출력하는 BufferedWriter
        StringTokenizer st = new StringTokenizer(br.readLine()," ");
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        cave = new char[R][C];
        //동굴에 대한 정보를 저장합니다.
        for(int i=R-1;i>=0;i--){
            String str = br.readLine();
            for(int j=0;j<C;j++)
                cave[i][j] = str.charAt(j);
        }
        N = Integer.parseInt(br.readLine());
        H = new int[N];
        st = new StringTokenizer(br.readLine()," ");
        //막대기에 높이를 저장합니다.
        for(int i=0;i<N;i++)
            H[i] = Integer.parseInt(st.nextToken());
        //각 막대기를 던져서 미네랄을 부수는 시뮬레이션 시작!
        for(int i=1;i<=N;i++){
            boolean check = false;
            int height = H[i-1]-1;
            int x=-1;
            if(i%2==1){		//홀수일 때 왼쪽 -> 오른쪽
                for(int j=0;j<C;j++){
                    if(cave[height][j]=='x'){
                        check = true;
                        x = j;
                        break;
                    }
                }
            }else{		//짝수일 때 오른쪽 -> 왼쪽
                for(int j=C-1;j>=0;j--){
                    if(cave[height][j]=='x'){
                        check = true;
                        x = j;
                        break;
                    }
                }
            }
            if(check){		//미네랄을 부수었을 때
                cave[height][x] = '.';
                for(int j=0;j<4;j++){
                    int tempX = x + dx[j];
                    int tempY = height + dy[j];
                    //부순 공간으로 근처 미네랄 탐색!(공중에 떠있는지 확인 후 내려가기)
                    if(inCaveCheck(tempX,tempY) && cave[tempY][tempX]=='x'){
                        bfs(tempX,tempY);
                    }

                }
            }
        }
        //모든 막대기를 던진 후 동굴에 정보를 BufferedWriter 저장
        for(int i=R-1;i>=0;i--){
            for(int j=0;j<C;j++)
                bw.write(cave[i][j]);
            bw.newLine();
        }
        bw.flush();		//결과 출력
        bw.close();
        br.close();
    }
    //미네랄 탐색 및 떠있는 미네랄 묶음 확인
    static void bfs(int x, int y){
        ArrayList<position> list = new ArrayList<>();
        Queue<position> queue = new LinkedList<>();
        boolean[][] visited = new boolean[R][C];
        visited[y][x] = true;
        list.add(new position(x,y));
        queue.add(new position(x, y));
        while(!queue.isEmpty()){
            position cur = queue.poll();
            if(cur.y == 0)		//떠있는 미네랄 묶음이 아닐 때
                return;
            for(int i=0;i<4;i++){
                int tempX = cur.x + dx[i];
                int tempY = cur.y + dy[i];
                if(inCaveCheck(tempX,tempY) && !visited[tempY][tempX]){
                    if(cave[tempY][tempX] == 'x'){	//인접한 미네랄이 존재시
                        visited[tempY][tempX] = true;
                        list.add(new position(tempX,tempY));
                        queue.add(new position(tempX,tempY));
                    }
                }
            }
        }
        //떠있는 미네랄 '.'으로 치환
        for(int i=0;i<list.size();i++){
            int tempX = list.get(i).x;
            int tempY = list.get(i).y;
            cave[tempY][tempX] = '.';
        }
        mineralDown(list);		//미네랄 내려가기 진행 함수 발동
    }
    //동굴 범위내에 있는 지 확인하는 함수
    static boolean inCaveCheck(int x, int y){
        if(x>=0 && y>=0 && y<R && x<C)
            return true;
        return false;
    }
    //미네랄이 떨어질 때 밑바닥에 닿거나 다른 미네랄이 닿을 때까지 내려가는 함수
    static void mineralDown(ArrayList<position> list){
        boolean check = true;
        //좌표 내려가기 진행
        while(check) {
            for (int i = 0; i < list.size(); i++) {
                int x = list.get(i).x;
                int y = list.get(i).y - 1;
                if (y == -1 || cave[y][x] == 'x')
                    check = false;
                list.set(i, new position(x, y));
            }
        }
        //내려간 좌표에 대하여 미네랄로 바꾸기
        for(int i=0;i<list.size();i++)
            cave[list.get(i).y+1][list.get(i).x] = 'x';
    }
}