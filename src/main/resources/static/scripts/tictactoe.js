const BOARD_AREA = 9;

const tictactoe = document.getElementById("tictactoe");
const boardElements = [];

let game = {
    board: [
        0, 0, 0,
        0, 0, 0,
        0, 0, 0
    ],
    winner: 0
};

const SYMBOL = [
    "", "X", "O"
];
const SYMBOL_ID = {
    "": 0,
    "NONE": 0,
    "X": 1,
    "O": 2
};

function placeSymbol(index) {
    if (game.board[index] !== 0) {
        return;
    }

    game.board[index] = 1;
    updateBoard();

    fetch("http://localhost:8080/api/tictactoe", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(game)
    }).then(response => {
        if (response.status !== 200) {
            return;
        }

        response.json().then(data => {
            for (let i = 0; i < BOARD_AREA; i++) {
                game.board[i] = SYMBOL_ID[data.board[i]];
            }
            game.winner = data.winner;

            updateBoard();
        });
    });
}

function updateBoard() {
    for (let i = 0; i < BOARD_AREA; i++) {
        boardElements[i].textContent = SYMBOL[game.board[i]];
    }

    console.log(game.winner);
}

function initBoard() {
    for (let i = 0; i < BOARD_AREA; i++) {
        const space = tictactoe.appendChild(document.createElement("div"));
        space.onclick = () => { placeSymbol(i); };
        boardElements.push(space);
    }

    updateBoard();
}

initBoard();