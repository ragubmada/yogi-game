# Yogi Game

## Game Overview

This project implements a Yogi Bear themed game based on the classic cartoon character. Players control Yogi Bear in Yellowstone National Park, navigating through a maze-like environment to collect picnic baskets while avoiding park rangers.

![Yogi Game](https://i.gyazo.com/f965ae2a4bfe533a566d12e005410114.png)

![Yogi Game Highscores](https://i.gyazo.com/6866d8eafd95c602fe14648907561820.png)

### Key Features:
- Navigate Yogi through a park filled with mountains and trees
- Collect picnic baskets to score points
- Avoid patrolling park rangers
- Multiple game levels
- High score system with database integration

## Gameplay Mechanics

- Yogi starts with 3 life points
- Rangers patrol horizontally or vertically
- If Yogi moves within one tile of a ranger, he loses a life point
- Losing a life sends Yogi back to the park entrance
- Collect all baskets to advance to a new level
- Game ends when all lives are lost

## Technical Details

- Implemented in Java using Swing for GUI
- Randomly selects from 10 pre-designed game boards
- MySQL database integration for high scores

### Database Configuration:
- MySQL server on port 3306
  - Login name is assumed to be `root`
  - Password is assumed to be `admin`
- Database name: `highscore`
- Table name: `highscores`
- Schema:
  ```sql
  CREATE TABLE highscores (
    timestamp TIMESTAMP PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    score INT
  );
  ```
- Uses MySQL Connector/J for database connectivity (included in the repository)

## Features

- Menu option to view top 10 highest scores
- Start a new game at any time
- Save high scores with player names

## License

This project is [MIT](https://choosealicense.com/licenses/mit/) licensed.
