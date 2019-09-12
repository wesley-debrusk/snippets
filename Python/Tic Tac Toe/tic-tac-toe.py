import random
from itertools import product


def make_board():
    board = [[' ', ' ', ' '], [' ', ' ', ' '], [' ', ' ', ' ']]
    return board


def make_tutorial_board():
    board = [['1', '2', '3'], ['4', '5', '6'], ['7', '8', '9']]
    return board


def print_board(board):
    for x in range(3):
        print('{' + board[x][0] + '} {' + board[x][1] + '} {' + board[x][2] + '}')


def update_board(board, int1, int2, character):
    if board[int1][int2] == ' ':
        board[int1][int2] = character
    else:
        print('Space is already occupied, please choose again.')
        board = user_turn(board, character)


def user_turn(board, character):
    choice = input('Please enter your choice of position: ')
    if choice == '1':
        update_board(board, 0, 0, character)
    elif choice == '2':
        update_board(board, 0, 1, character)
    elif choice == '3':
        update_board(board, 0, 2, character)
    elif choice == '4':
        update_board(board, 1, 0, character)
    elif choice == '5':
        update_board(board, 1, 1, character)
    elif choice == '6':
        update_board(board, 1, 2, character)
    elif choice == '7':
        update_board(board, 2, 0, character)
    elif choice == '8':
        update_board(board, 2, 1, character)
    elif choice == '9':
        update_board(board, 2, 2, character)
    else:
        print('Invalid option, please choose again.')
        user_turn(board, character)
    return board


def ai_turn(board, character):
    print('Your opponent has made a selection.')
    if board[1][1] == ' ':
        board[1][1] = character
    else:
        for i, j in product(range(3), range(3)):
            if board[i][j] == ' ':
                board[i][j] = character
                break
    return board


def check_board(board):
    if board[0][0] == board[0][1] == board[0][2] and board[0][0] != ' ':
        return board[0][0]
    elif board[1][0] == board[1][1] == board[1][2] and board[1][0] != ' ':
        return board[1][0]
    elif board[2][0] == board[2][1] == board[2][2] and board[2][0] != ' ':
        return board[2][0]
    elif board[0][0] == board[1][0] == board[2][0] and board[0][0] != ' ':
        return board[0][0]
    elif board[0][1] == board[1][1] == board[2][1] and board[0][1] != ' ':
        return board[0][1]
    elif board[0][2] == board[1][2] == board[2][2] and board[0][2] != ' ':
        return board[2][0]
    elif board[0][0] == board[1][1] == board[2][2] and board[0][0] != ' ':
        return board[0][0]
    elif board[0][2] == board[1][1] == board[2][0] and board[0][2] != ' ':
        return board[0][2]
    else:
        return 'continue'


def check_full(board):
    for i in range(3):
        for j in range(3):
            if board[i][j] == ' ':
                return False
    return True


def run_game():
    rand = random.randint(0, 1)
    character = ' '
    aicharacter = ' '
    gameboard = make_board()

    if rand == 0:
        print('You are player X, you go first.')
        character = 'X'
        aicharacter = 'O'
    elif rand == 1:
        print('You are player O, you will go second.')
        character = 'O'
        aicharacter = 'X'
        gameboard = ai_turn(gameboard, aicharacter)
        print_board(gameboard)

    while True:
        gameboard = user_turn(gameboard, character)
        check = check_board(gameboard)
        if check != 'continue':
            if check == character:
                print('You won!')
            else:
                print('Your opponent won.')
            break
        if check_full(gameboard):
            print('The game is a tie.')
            break
        print_board(gameboard)
        gameboard = ai_turn(gameboard, aicharacter)
        check = check_board(gameboard)
        if check != 'continue':
            if check == character:
                print('You won!')
            else:
                print('Your opponent won.')
            break
        if check_full(gameboard):
            print('The game is a tie.')
            break
        print_board(gameboard)


def run():
    print('Welcome to tic-tac-toe! Created by Wesley DeBrusk.')
    tutorialboard = make_tutorial_board()
    print('The positions of the game board are as follows:')
    print_board(tutorialboard)
    while True:
        run_game()
        playagain = input('Play again? (type "yes" to play again or anything else to exit): ')
        if playagain != 'yes':
            print('Thanks for playing!')
            break


run()