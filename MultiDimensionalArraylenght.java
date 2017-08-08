class MultiDimensionalArraylenght{
	public static void main(String[] args) {
		int[][] ar= new int[2][3];
		for(int i=0;i<2;i++){
			for(int j=0;j<3;j++){
				ar[i][j]=i*j;
			}
		}
		System.out.println(ar.length);
	}
}