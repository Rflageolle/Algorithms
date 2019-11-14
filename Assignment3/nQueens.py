import random
import numpy as np

global N
N = 8

def printSolution(board):
    for i in range(N):
        for j in range(N):
            print (board[i][j], end = " ")
        print()

def isSafe(board, row, col):

    # Check this row on left side
    for i in range(col):
        if board[row][i] == 1:
            # print("ROW COLLISION")
            return False

    # Check upper diagonal on left side
    for i, j in zip(range(row, -1, -1),
                    range(col, -1, -1)):
        if board[i][j] == 1:
            # print("UPPER DIAG COLLISION")
            return False

    # Check lower diagonal on left side
    for i, j in zip(range(row, N, 1),
                    range(col, -1, -1)):
        if board[i][j] == 1:
            # print("LOWER DIAG COLLISION")
            return False

    return True

def placeQueens(board, k):
    col = 0
    attempts = 0
    while (col < k):
        if (attempts == 1000000):
            print("backtracking cut in at ", col)
            backtrack(board, col)
            break

        row = random.randint(0, 7)
        if isSafe(board, row, col):
            board[row][col] = 1
            col = col + 1

        attempts = attempts + 1

def placeRandK(board, col, K):

    if (K > 0):
        if (col <= K):
            row = random.randint(0, 7)
            return placeRandK(board, col + 1, K)
        else:
            col = col - 1
            for i in range(N):
                if board[i][col] == 1:
                    board[i][col] = 0
                    if isSafe(board, i, col) == False:
                        return False
                    else:
                        board[i][col] = 1
                        if (col == 0):
                            return True
                        col = col - 1
                        continue

            if (K == 7):
                print("THIS IS WHAT IT THINKS THE SOLUTION IS: \n")
                printSolution(board)
                print()
                return True
        col = K + 1
        return solveNQUtil(board, col)

    elif (K == 0):
        return solveNQUtil(board, col)

def solveNQUtil(board, col):

        if col >= N:
            return True

        # Consider this column and try placing
        # THE REST of the queens in all rows one by one
        for i in range(N):

            # print()
            # printSolution(board)
            # print()
            emptyCol(board, col)
            if isSafe(board, i, col):

                # Place this queen in board[i][col]
                board[i][col] = 1

                # recur to place rest of the queens
                if solveNQUtil(board, col + 1) == True:
                    return True

                # If placing queen in board[i][col
                # doesn't lead to a solution, then remove
                # queen from board[i][col]
                board[i][col] = 0

        # if the queen can not be placed in any row in
        # this colum col then return false
        return False

def emptyCol(board, column):
    for i in range(N):
        if board[i][column] == 1:
            board[i][column] = 0
            break

# if there is no solution from this point. Remove queen from current row
# the go back one row and look for solutions
def backtrack(board, col):
    if solveNQUtil(board, col - 1) == True:
        return True
    else:
        print("removing the queen from column ", (col))
        backtrack(board, col - 1)


def placeK(board, k):
    placeQueens(board, k)
    backtrack(board, k)
    printSolution(board)

def main():
    board = [ [0, 0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0, 0],
              [0, 0, 0, 0, 0, 0, 0, 0] ]


    placeK(board, 8)


# Driver Code
if __name__== "__main__":
    main()
