# Chess in Java
This is a basic console-based Chess game written in Java.

The project was initially focused on implementing core Chess mechanics correctly. After that, the plan is to introduce a special "Duck" piece that can be moved after every turn to block the opponent, adding a unique twist to standard gameplay.

Currently, the game does **not** have a graphical interface. Instead, the board is printed to the console using standard Chess notation symbols:

- **White** pieces are represented by **uppercase** letters (e.g., _K_ for _King_, _P_ for _Pawn_).

- **Black** pieces are represented by **lowercase** letters (e.g., _k_ for _King_, _p_ for _Pawn_).

## Board Setup
If you want to change the starting position, you can modify the Board class, inside the Board() constructor. There, you can manually place any pieces you want.

The internal board is an 8x8 grid, with row and column indices from 0 to 7. Positions are read from the **bottom-left (0,0)** to the **top-right (7,7)**, arranged like this:
![WhatsApp Image 2025-04-28 at 21 52 22_8fcc3367](https://github.com/user-attachments/assets/b504eb44-8ff9-40e2-9c45-6830c2f92d64)

When playing the game, however, you will input moves using **standard chess notation** (for example: _e2 e4_).

## How to Run
To run this program:

- Use your favorite Java compiler or IDE.

- The project has been tested on **IntelliJ IDEA** with the latest version of Java at the time of development.
