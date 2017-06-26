package test0102;

public class Offset {

	public Offset(int oCol, int oRow) {
		// TODO Auto-generated constructor stub
		col = oCol;
		row = oRow;
	}

	public void aStarInit(int aStarRow, int aStarCol, int aStarG, int aStarH) {
		this.row = aStarRow;
		this.col = aStarCol;
		this.g = aStarG;	
		this.h = aStarH;
		this.f = aStarG + aStarH;	
	}

	int col;
	int row;
	int direction;

	int parentCol;	//ºÎ¸ðÁÂÇ¥
	int parentRow;

	double f;
	double g;
	double h;

}
