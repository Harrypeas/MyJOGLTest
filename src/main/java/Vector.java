import glm.mat._3.Mat3;

public class Vector {
	public static void main(String[] args)
	{
		float []matrix1 = {1.0f, 2.0f, 3.0f,
						2.0f, 3.0f, 4.0f,
						3.0f, 4.0f, 5.0f};
		Mat3 mat1 = new Mat3(matrix1);
		
		Mat3 mat2 = new Mat3(matrix1);
		
		Mat3 mat3 = mat1.mul(mat2);
		mat3.print();
		
		mat1.transpose().print();
	}
}
