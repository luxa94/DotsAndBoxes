package rs.ac.uns.ftn.luxa.dotsandboxes.model;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Player;
import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Status;
import rs.ac.uns.ftn.luxa.dotsandboxes.model.board_field_impl.Dot;
import rs.ac.uns.ftn.luxa.dotsandboxes.model.board_field_impl.Edge;
import rs.ac.uns.ftn.luxa.dotsandboxes.model.board_field_impl.Field;
import rs.ac.uns.ftn.luxa.dotsandboxes.util.IntIntTuple;

public class GameNode {

    private static int n;
    private static int nn;
    public static int maxScore;

    private int score;

    private Player player;

    private BoardField[][] fields;

    public GameNode(int n) {
        GameNode.n = n;
        nn = 2 * n + 1;
        maxScore = n * n;

        fields = new BoardField[nn][nn];
        for (int i = 0; i < nn; i++) {
            final int ii = i % 2;
            for (int j = 0; j < nn; j++) {
                final int jj = j % 2;
                if (ii == 0 && jj == 0) {
                    fields[i][j] = new Dot(i, j);
                } else if (ii == 1 && jj == 1) {
                    fields[i][j] = new Field(i, j);
                } else {
                    fields[i][j] = new Edge(i, j);
                }
            }
        }
        connectParts();
    }

    public GameNode(GameNode parentNode) {
        fields = new BoardField[nn][nn];
        for (int i = 0; i < nn; i++) {
            final int ii = i % 2;
            for (int j = 0; j < nn; j++) {
                final int jj = j % 2;
                if (ii == 0 && jj == 0) {
                    fields[i][j] = parentNode.fields[i][j];
                } else if (ii == 1 && jj == 1) {
                    fields[i][j] = new Field(i, j);
                    fields[i][j].setStatus(parentNode.fields[i][j].getStatus());
                } else {
                    fields[i][j] = new Edge(i, j);
                    fields[i][j].setStatus(parentNode.fields[i][j].getStatus());
                }
            }
        }
        connectParts();
    }

    private void connectParts() {
        for (int i = 0; i < nn; i++) {
            final int ii = i % 2;
            for (int j = 0; j < nn; j++) {
                final int jj = j % 2;
                if (ii == 1 && jj == 1) {
                    fields[i][j].addAdjacent(fields[i + 1][j]);
                    fields[i][j].addAdjacent(fields[i - 1][j]);
                    fields[i][j].addAdjacent(fields[i][j + 1]);
                    fields[i][j].addAdjacent(fields[i][j - 1]);
                } else if (ii == 0 && jj == 1) {
                    if (i > 0) {
                        fields[i][j].addAdjacent(fields[i - 1][j]);
                    }
                    if (i < nn - 1) {
                        fields[i][j].addAdjacent(fields[i + 1][j]);
                    }
                } else if (ii == 1 && jj == 0) {
                    if (j > 0) {
                        fields[i][j].addAdjacent(fields[i][j - 1]);
                    }
                    if (j < nn - 1) {
                        fields[i][j].addAdjacent(fields[i][j + 1]);
                    }
                }
            }
        }
    }

    public List<GameNode> getNextNodes(Status status) {
        final List<GameNode> nextNodes = new ArrayList<>();

        for (IntIntTuple tuple : findFreeEdgeTuples()) {
            final GameNode nextNode = new GameNode(this);
            nextNode.getFields()[tuple.getFirst()][tuple.getSecond()].setStatus(status);
//            if (completedFields() != nextNode.completedFields()) {
//                final List<GameNode> moreNodes = nextNode.getNextNodes(status);
//                if (moreNodes.size() == 0) {
//                    nextNodes.add(nextNode);
//                }
//                nextNodes.addAll(nextNode.getNextNodes(status));
//            } else {
//                nextNodes.add(nextNode);
//            }
            nextNodes.add(nextNode);
        }

        return nextNodes;
    }

    public List<IntIntTuple> findFreeEdgeTuples() {
        final List<IntIntTuple> free = new ArrayList<>();
        for (int i = 0; i < nn; ++i) {
            final int ii = i % 2;
            for (int j = 1 - ii; j < nn; j += 2) {
                if (fields[i][j].getStatus() == Status.FREE) {
                    free.add(new IntIntTuple(i, j));
                }
            }
        }
        return free;
    }

    public List<IntIntTuple> findDifferentEdges(GameNode other) {
        final List<IntIntTuple> free = new ArrayList<>();
        for (int i = 0; i < nn; ++i) {
            final int ii = i % 2;
            for (int j = 1 - ii; j < nn; j += 2) {
                if (fields[i][j].getStatus() != other.getFields()[i][j].getStatus()) {
                    free.add(new IntIntTuple(i, j));
                }
            }
        }
        return free;
    }


    public int completedFields() {
        int score = 0;
        for (int i = 1; i < nn; i += 2) {
            for (int j = 1; j < nn; j += 2) {
                if (fields[i][j].isCompleted()) {
                    score++;
                }
            }
        }
        return score;
    }

    public List<IntIntTuple> findDifferentFields(GameNode other) {
        final List<IntIntTuple> free = new ArrayList<>();
        for (int i = 1; i < nn; i += 2) {
            for (int j = 1; j < nn; j += 2) {
                if (fields[i][j].getStatus() != other.getFields()[i][j].getStatus()) {
                    free.add(new IntIntTuple(i, j));
                }
            }
        }
        return free;
    }

    public int getScoreDifference() {
        int score = 0;
        for (int i = 1; i < nn; i += 2) {
            for (int j = 1; j < nn; j += 2) {
                if (fields[i][j].getStatus() == Status.PLAYERS) {
                    score--;
                } else if (fields[i][j].getStatus() == Status.COMPUTERS) {
                    score++;
                }
            }
        }
        return score;
    }

    public int getScoreAdjusted() {
        int score = 0;
        for (int i = 1; i < nn; i += 2) {
            for (int j = 1; j < nn; j += 2) {
                if (fields[i][j].getStatus() == Status.PLAYERS) {
                    score -= 10;
                } else if (fields[i][j].getStatus() == Status.COMPUTERS) {
                    score++;
                }
            }
        }
        return score;
    }

    public int getComputersScore() {
        int score = 0;
        for (int i = 1; i < nn; i += 2) {
            for (int j = 1; j < nn; j += 2) {
                if (fields[i][j].getStatus() == Status.COMPUTERS) {
                    score++;
                }
            }
        }
        return score;
    }

    public int getPlayersScore() {
        int score = 0;
        for (int i = 1; i < nn; i += 2) {
            for (int j = 1; j < nn; j += 2) {
                if (fields[i][j].getStatus() == Status.PLAYERS) {
                    score++;
                }
            }
        }
        return score;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < nn; i++) {
            for (int j = 0; j < nn; j++) {
                builder.append(fields[i][j].toString());
                builder.append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getN() {
        return n;
    }

    public BoardField[][] getFields() {
        return fields;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addToScore(int score) {
        this.score += score;
    }
}
