package test0102;

import java.util.LinkedList;

public class MazeAstar {

	static int dir = 8;
	static Offset[] move = new Offset[dir];
	static LinkedList<Offset> path = new LinkedList<Offset>();
	static LinkedList<Offset> open = new LinkedList<Offset>();
	static LinkedList<Offset> closed = new LinkedList<Offset>();

	static Offset dest = new Offset(3, 3);
	static Offset start = new Offset(1, 1);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Offset nowPos = new Offset(1, 1);
		Offset result = new Offset(1, 1);
		Offset mTemp = new Offset(1, 1);

		boolean wayOut = true;

		int nextPos; // 다음위치
		int shortF;

		int mazeRowSize = 7; // 미로 크기
		int mazeColSize = 7;

		move[0] = new Offset(0, -1);
		move[1] = new Offset(1, -1);
		move[2] = new Offset(1, 0);
		move[3] = new Offset(1, 1);
		move[4] = new Offset(0, 1);
		move[5] = new Offset(-1, 1);
		move[6] = new Offset(-1, 0);
		move[7] = new Offset(-1, -1);

		int[][] maze = new int[mazeRowSize][mazeColSize]; // 랜덤한 미로 생성하기 위한 2차원
															// 배열
		int[][] mark = new int[mazeRowSize][mazeColSize]; // 탐색한 길을 마크하기 위한 2차원
															// 배열
		dest.row = mazeRowSize - 2; // 출구값 설정
		dest.col = mazeColSize - 2;

		makeMaze(maze); // 미로 생성

		printMaze(maze); // 생성한 미로 출력

		// ---------------------------------- 초기설정 완료

		nowPos.row = start.row;
		nowPos.col = start.col; // 시작값을 현재 위치로 저장

		mTemp.aStarInit(start.row, start.col, 0, (dest.row - start.row) + (dest.col - start.col));

		closed.add(mTemp); // 시작값을 닫힌 목록으로
		addOpenListInit(maze, nowPos); // 시작값의 인접한 노드들을 열린목록으로
		// open.remove(1);

		while (open.isEmpty() != true) {
			shortF = shortestF(); // 열린목록의 가장 낮은 F값을 가진 인덱스값 저장
			nowPos = open.remove(shortF); // 열린목록의 가장낮은 F값을 가진 리스트를 가져온 뒤 삭제
			closed.add(nowPos); // 현재 위치를 닫힌목록으로 추가
			addOpenList(maze, nowPos);
			System.out.println("while문 끝");
			if (destInOpenListCheck() == true) {
				System.out.println("목적지 도달");
				printAstarMaze();
				break;
			}
		}

	}

	public static void printAstarMaze() {
		LinkedList<Offset> pTempOpen = new LinkedList<Offset>(); // 열린 임시 목록
		LinkedList<Offset> pTempClosed = new LinkedList<Offset>(); // 닫힌 임시 목록
		Offset pTemp = new Offset(0, 0);
		Offset pNowPos = new Offset(0, 0);

		pTempOpen = (LinkedList<Offset>) open.clone(); // 열린목록 복사

		for (int i = 0, pTempOpenSize = pTempOpen.size(); i < pTempOpenSize; i++) {
			pTemp = pTempOpen.removeLast();
			if (pTemp.row == dest.row && pTemp.col == dest.col) {
				System.out.println("(" + pTemp.row + ", " + pTemp.col + ")");
				pNowPos = pTemp;
			}
		}
		while (pNowPos.row != start.row || pNowPos.col != start.col) {
			pTempClosed = (LinkedList<Offset>) closed.clone();

			for (int i = 0, pTempClosedSize = pTempClosed.size(); i < pTempClosedSize; i++) {
				pTemp = pTempClosed.removeLast();
				if (pNowPos.parentRow == pTemp.row && pNowPos.parentCol == pTemp.col) {
					System.out.println("(" + pTemp.row + ", " + pTemp.col + ")");
					pNowPos = pTemp;
				}
			}
		}

	}

	public static boolean destInOpenListCheck() {
		LinkedList<Offset> dTempOpen = new LinkedList<Offset>();
		Offset dTemp = new Offset(0, 0);
		dTempOpen = (LinkedList<Offset>) open.clone();

		for (int i = 0, dTempOpenSize = dTempOpen.size(); i < dTempOpenSize; i++) {
			dTemp = dTempOpen.removeFirst();
			if (dest.row == dTemp.row && dest.col == dTemp.col) {
				return true;
			}
		}

		return false;
	}

	public static int shortestF() {
		LinkedList<Offset> sTempOpen = new LinkedList<Offset>();
		sTempOpen = (LinkedList<Offset>) open.clone();
		Offset sTemp = new Offset(1, 1);
		double sMin = sTempOpen.getFirst().f;
		int openIndex = 0;

		for (int i = 0, sTempOpenSize = sTempOpen.size(); i < sTempOpenSize; i++) {
			sTemp = sTempOpen.poll();
			if (sMin > sTemp.f) {
				sMin = sTemp.f;
				openIndex = i;
			}
		}
		System.out.print("F 가장 작은 값과 좌표: ");
		System.out
				.println(open.get(openIndex).f + "(" + open.get(openIndex).row + ", " + open.get(openIndex).col + ")");

		return openIndex;
	}

	// 열린목록 추가 매소드
	public static int addOpenList(int[][] aSearchMaze, Offset aNowPos) {
		boolean[] aMove = new boolean[dir];
		LinkedList<Offset> aTempClosed = new LinkedList<Offset>();

		for (int i = 0; i < dir; i++) {
			aMove[i] = true;
		}
		// 갈수 없는 곳 저장
		for (int i = 0; i < dir; i++) {
			if (i % 2 == 0 && aSearchMaze[aNowPos.row + move[i].row][aNowPos.col + move[i].col] == 1) {
				if (i == 0) {
					aMove[1] = false;
					aMove[0] = false;
					aMove[7] = false;
				} else {
					aMove[i - 1] = false;
					aMove[i] = false;
					aMove[i + 1] = false;
				}
			} else if (aSearchMaze[aNowPos.row + move[i].row][aNowPos.col + move[i].col] == 1) {
				aMove[i] = false;
			}
		}

		aTempClosed = (LinkedList<Offset>) closed.clone();

		for (int i = 0, aTempClosedSize = aTempClosed.size(); i < aTempClosedSize; i++) {
			Offset aTemp = new Offset(1, 1);
			aTemp = aTempClosed.removeLast();
			for (int j = 0; j < dir; j++) {
				if (aNowPos.row + move[j].row == aTemp.row && aNowPos.col + move[j].col == aTemp.col) {
					aMove[j] = false;
				}
			}
		}

		for (int i = 0; i < dir; i++) {
			Offset aTemp = new Offset(0, 0);

			LinkedList<Offset> aTempOpen = new LinkedList<Offset>();

			boolean openExist = false;

			aTempOpen = (LinkedList<Offset>) open.clone();
			// 열린목록에 존재하는지 검사
			if (aMove[i] == true) {
				for (int j = 0, aTempOpenSize = aTempOpen.size(); j < aTempOpenSize; j++) {
					aTemp = aTempOpen.removeLast();
					if (aNowPos.row + move[i].row == aTemp.row && aNowPos.col + move[i].col == aTemp.col) {
						openExist = true;
					}
				}
			}
			// 열린목록에 없을 때 열린목록에 추가한뒤 부모정보 추가(현재 위치가 부모)
			if (aMove[i] == true && openExist == false) {
				Offset aTemp2 = new Offset(0, 0);
				aTemp2.row = aNowPos.row + move[i].row;
				aTemp2.col = aNowPos.col + move[i].col;
				aTemp2.parentRow = aNowPos.row;
				aTemp2.parentCol = aNowPos.col;
				aTemp2.g = ((i % 2 == 0) ? 1 : 1.4) + aNowPos.g;
				aTemp2.h = dest.row - (aNowPos.row + move[i].row) + (dest.col - (aNowPos.col + move[i].col));
				aTemp2.f = aTemp2.g + aTemp2.h;
				aTemp2.direction = i;
				open.add(aTemp2);
			}
			// 열린목록에 있다면 비용비교
			if (aMove[i] == true && openExist == true) {
				aTempOpen = (LinkedList<Offset>) open.clone();
				Offset aTemp2 = new Offset(0, 0);
				for (int j = 0, aTempOpenSize = aTempOpen.size(); j < aTempOpenSize; j++) {
					aTemp = aTempOpen.removeFirst();
					if (aNowPos.row + move[i].row == aTemp.row && aNowPos.col + move[i].col == aTemp.col) {
						if ((((i % 2 == 0) ? 1 : 1.4) + aNowPos.g) < aTemp.g) {
							open.remove(j);
							aTemp2.row = aNowPos.row + move[i].row;
							aTemp2.col = aNowPos.col + move[i].col;
							aTemp2.parentRow = aNowPos.row;
							aTemp2.parentCol = aNowPos.col;
							aTemp2.g = ((i % 2 == 0) ? 1 : 1.4) + aNowPos.g;
							aTemp2.h = dest.row - (aNowPos.row + move[i].row)
									+ (dest.col - (aNowPos.col + move[i].col));
							aTemp2.f = aTemp.g + aTemp.h;
							aTemp2.direction = i;
							open.add(aTemp2);
						}
					}
				}

			}
		}

		return 0;
	}

	// 열린목록에 추가
	public static void addOpenListInit(int[][] aMaze, Offset nowPos) {
		boolean[] aMove = new boolean[dir];
		LinkedList<Offset> aTempClosed = new LinkedList<Offset>();
		Offset aTemp2 = new Offset(0, 0);
		aTempClosed = (LinkedList<Offset>) closed.clone();

		for (int i = 0, aTempClosedSize = aTempClosed.size(); i < aTempClosedSize; i++) {
			aTemp2 = aTempClosed.removeFirst();
			if (aTemp2.col == nowPos.col && aTemp2.row == nowPos.row) {
				break;
			}
		}

		for (int i = 0; i < dir; i++) {
			aMove[i] = true;
		}

		// 갈수 없는 곳 저장
		for (int i = 0; i < dir; i += 2) {
			if (aMaze[aTemp2.row + move[i].row][aTemp2.col + move[i].col] == 1) {
				if (i == 0) {
					aMove[1] = false;
					aMove[0] = false;
					aMove[7] = false;
				} else {
					aMove[i - 1] = false;
					aMove[i] = false;
					aMove[i + 1] = false;
				}
			}
		}
		for (int i = 0; i < dir; i++) {
			if (aMove[i] == true) {
				Offset aTemp = new Offset(0, 0);
				aTemp.row = aTemp2.row + move[i].row;
				aTemp.col = aTemp2.col + move[i].col;
				aTemp.parentRow = aTemp2.row;
				aTemp.parentCol = aTemp2.col;
				aTemp.g = ((i % 2 == 0) ? 1 : 1.4) + aTemp2.g;
				aTemp.h = dest.row - (aTemp2.row + move[i].row) + (dest.col - (aTemp2.col + move[i].col));
				aTemp.f = aTemp.g + aTemp.h;
				aTemp.direction = i;
				open.add(aTemp);
			}
		}

	}

	// 생성한 미로 출력 메소드
	public static void printMaze(int[][] pMaze) {
		// 생성한 미로 출력
		for (int i = 0; i < pMaze.length; i++) {
			for (int j = 0; j < pMaze[0].length; j++) {
				System.out.print(pMaze[i][j]);
			}
			System.out.println();
		}
	}

	// 미로 생성 메소드
	public static void makeMaze(int[][] mMaze) {
		for (int i = 0; i < mMaze.length; i++) {
			for (int j = 0; j < mMaze[0].length; j++) {
				if (i == 0 || i == mMaze.length - 1 || j == 0 || j == mMaze[0].length - 1) {
					mMaze[i][j] = 1;
				} else if (i == 2 && j == 3) {
					mMaze[i][j] = 1;
				} else if (i == 3 && j == 3) {
					mMaze[i][j] = 1;
				} else if (i == 4 && j == 2) {
					mMaze[i][j] = 1;
				} else if (i == 5 && j == 4) {
					mMaze[i][j] = 1;
				} else {
					mMaze[i][j] = 0;
				}
			}
		}
	}
}
