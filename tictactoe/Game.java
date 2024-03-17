package ru.gb.jcore.tictactoe;

import java.util.Random;
import java.util.Scanner;

public class Game {
    //private static final int WIN_COUNT = 3;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '.';

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private static char[][] field;
    private static final int SIZE_X = 4;
    private static final int SIZE_Y = 4;

    public static void main(String[] args) {
        initialize();
        printField();
        while(true) {
            humanTurn();
            printField();
            if (gameCheck(DOT_HUMAN, "You won!"))
                break;

            aiTurn();
            printField();
            if (gameCheck(DOT_AI, "Computer won!"))
                break;
        }
    }

    private static void initialize(){
        field = new char[SIZE_X][SIZE_Y];
        for(int x = 0; x < SIZE_X; x++){
            for(int y = 0; y < SIZE_Y; y++){
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < SIZE_X * 2 + 1; i++){
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        }
        System.out.println();

        for (int i = 0; i < SIZE_X; i++){
            System.out.print(i + 1 + "|");

            for (int j = 0; j <  SIZE_Y; j++)
                System.out.print(field[i][j] + "|");

            System.out.println();
        }

        for (int i = 0; i < SIZE_X * 2 + 2; i++){
            System.out.print("-");
        }
        System.out.println();
    }

    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Enter the coordinates Х и Y (1 to" +
                    SIZE_Y +  ") space separated: ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while(!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    private static boolean isCellValid(int x, int y){
        return x >= 0 && x < SIZE_X
                && y >= 0 && y < SIZE_Y;
    }

    private static void aiTurn() {
        if (aiTrueTurn()) return; //проверка хорошего хода
        int x, y;
        do {
            x = RANDOM.nextInt(SIZE_X);
            y = RANDOM.nextInt(SIZE_Y);
        } while(!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    //метод проверяет есть ли хороший ход
    // если есть то выполняет ход
    private static boolean aiTrueTurn() {
        int [] true_turn = goodDot(DOT_AI);
        if (true_turn[0]>-1) {
            field[true_turn[0]][true_turn[1]]=DOT_AI;
            return true;
        }
        true_turn = goodDot(DOT_HUMAN);
        if (true_turn[0]>-1){
            field[true_turn[0]][true_turn[1]]=DOT_AI;
            return true;
        }
        return false;
    }


    //возвращает координаты хорошего хода в массиве
    private static int[] goodDot(char symbol) {
        int count_symbol_in_line = 0;
        int true_x=-1, true_y=-1;
        // Поиск первого хорошего хода по всем горизонталям
        for(int x=0; x<SIZE_X; x++ ){
            count_symbol_in_line = 0;
            true_x=-1;
            true_y=-1;
            for (int y=0; y<SIZE_Y; y++){
                if (symbol==field[x][y]) count_symbol_in_line++;
                else if (field[x][y]==DOT_EMPTY) {
                    true_x = x;
                    true_y = y;
                }
            }
            if ((count_symbol_in_line==(SIZE_Y-1))&&(true_x>-1)) {
                return new int[] {true_x,true_y};}
        }

        // Поиск первого хорошего хода по вертикалям
        for (int x = 0; x < SIZE_X; x++) {
            count_symbol_in_line = 0;
            true_x=-1;
            true_y=-1;
            for (int y = 0; y < SIZE_Y; y++) {
                if (symbol == field[y][x]) count_symbol_in_line++;
                else if (field[y][x]==DOT_EMPTY) {
                    true_x = y;
                    true_y = x;
                }
            }
            if ((count_symbol_in_line==(SIZE_Y-1))&&(true_x>-1)) {
                return new int[] {true_x,true_y};}
            }

        count_symbol_in_line = 0;
        true_x=-1;
        true_y=-1;

        // Поиск первого хорошего хода главной диагонали
        for (int y = 0; y < SIZE_Y; y++) {
            if (symbol == field[y][y]) count_symbol_in_line++;
            else if (field[y][y] == DOT_EMPTY) {
                true_x = y;
                true_y = y;
            }

        }
        if ((count_symbol_in_line==(SIZE_Y-1))&&(true_x>-1)) {
            return new int[] {true_x,true_y};}

        count_symbol_in_line = 0;
        true_x=-1;
        true_y=-1;

        // Проверка по побочной диагонали
        for (int y = 0; y < SIZE_Y; y++) {
            if (symbol == field[y][SIZE_Y - 1 - y]) count_symbol_in_line++;
            else if (field[y][SIZE_Y - 1 - y]==DOT_EMPTY) {
                true_x = y;
                true_y = (SIZE_Y - 1 - y);
            }

        }
        if ((count_symbol_in_line==(SIZE_Y-1))&&(true_x>-1)) {
            return new int[] {true_x,true_y};
        }

        return new int[] {-1,-1};
    }

    private static boolean gameCheck(char symbol, String message){
        if(checkWin(symbol)){
            System.out.println(message);
            return true;
        }
        if(checkDraw()){
            System.out.println("Draw");
            return true;
        }
        return false;
    }

    private static boolean checkDraw() {
        for(int x = 0; x < SIZE_X; x++){
            for(int y = 0; y < SIZE_Y; y++){
               if(isCellEmpty(x, y)) return false;
            }
        }

        return true;
    }

    private static boolean checkWin(char symbol) {
//        // Проверка по трем горизонталям
//        if (field[0][0] == symbol && field[0][1] == symbol && field[0][2] == symbol) return true;
//        if (field[1][0] == symbol && field[1][1] == symbol && field[1][2] == symbol) return true;
//        if (field[2][0] == symbol && field[2][1] == symbol && field[2][2] == symbol) return true;
//
//        // Проверка по диагоналям
//        if (field[0][0] == symbol && field[1][1] == symbol && field[2][2] == symbol) return true;
//        if (field[0][2] == symbol && field[1][1] == symbol && field[2][0] == symbol) return true;
//
//        // Проверка по трем вертикалям
//        if (field[0][0] == symbol && field[1][0] == symbol && field[2][0] == symbol) return true;
//        if (field[0][1] == symbol && field[1][1] == symbol && field[2][1] == symbol) return true;
//        if (field[0][2] == symbol && field[1][2] == symbol && field[2][2] == symbol) return true;

        return  cycleCheckWin(symbol);//false;
    }
    private static boolean cycleCheckWin(char symbol) {
        // Проверка по всем горизонталям
        for(int x=0; x<SIZE_X; x++ ){
            for (int y=0; y<SIZE_Y; y++){
                if (!(symbol==field[x][y])) break;
                if (y==(SIZE_Y-1)) return true;
            }
        }

        // Проверка по вертикалям
        for(int x=0; x<SIZE_X; x++ ){
            for (int y=0; y<SIZE_Y; y++){
                if (!(symbol==field[y][x])) break;
                if (y==(SIZE_Y-1)) return true;
            }
        }

        // Проверка по главной диагонали
        for (int y = 0; y < SIZE_Y; y++) {
            if (!(symbol==field[y][y])) break;
            if (y==(SIZE_Y-1)) return true;
        }

        // Проверка по побочной диагонали
        for (int y = 0; y < SIZE_Y; y++) {
            if (!(symbol==field[y][SIZE_Y-1-y])) break;
            if (y==(SIZE_Y-1)) return true;
        }
        return false;
    }
}
