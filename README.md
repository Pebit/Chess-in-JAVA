This is Chess in Java
It is intended, after figuring out the basic Chess mechanics, to add an extra duck piece that you can move after every piece move to block your adversary.
The game does not have a graphical interface so far. To visualise the board I have made the pieces be their symbols/notations. For black the pieces are lowercase and for white they are uppercase.
To change the board initial setup you can go to the Board class and inside Board() add whatever pieces you may want.
The board positions are from 0 to 7 for rows and columns and are read from down left to up right so it looks like this:
![WhatsApp Image 2025-04-28 at 21 52 22_8fcc3367](https://github.com/user-attachments/assets/b504eb44-8ff9-40e2-9c45-6830c2f92d64)

When playing the game, however, you will input the positions as in normal chess (ex. a1 -> h8)
To run this program use your favorite Java compiler. I only tested this build on IntelliJ Idea on the latest version of Java if I'm not mistaken.
