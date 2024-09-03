import java.util.*;
class Solution {
    
    static PriorityQueue<int[]> pq;
    static List<int[]> list[];
    
    public int solution(int n, int[][] costs) {
        int answer = 0;
        list = new ArrayList[n];
        
        for(int i=0; i<n; i++)
            list[i]=new ArrayList<>();
      
        pq = new PriorityQueue<>(new Comparator<int[]>(){
         
        public int compare(int[] o, int[] o2){
                return o[1] - o2[1];
            }
        });
        
        for(int[] cost: costs){
            
            list[cost[0]].add(new int[]{cost[1], cost[2]});
            list[cost[1]].add(new int[]{cost[0], cost[2]});
            
        }
        
        boolean visited[] = new boolean[n];
        pq.add(new int[]{0,0});
        
        while(!pq.isEmpty()){
            
            int[] arr = pq.poll();

            if(visited[arr[0]]) continue;
        
            visited[arr[0]] = true;
            
            answer += arr[1];
            
            for(int node[]: list[arr[0]]){
                if(!visited[node[0]]){
                    pq.add(new int[]{node[0], node[1]});
                }
            }
        }
        
        return answer;
    }
}