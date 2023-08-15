package ro.ulbs.ai.homework.strategies;

import ro.ulbs.ai.homework.history.History;
import ro.ulbs.ai.homework.history.Round;

public class DecisionTreeStrategy extends Strategy {
    private static final double TRESHOLD = 0.001;
    private Node root = new Node(0);

    public DecisionTreeStrategy(History history, String strategyName) {
        super(history, strategyName);
    }

    @Override
    public boolean play() {
        if (history.getNumberOfRounds() > 1) {
            root.update(history.getNumberOfRounds() - 1);
        }
        Node leaf = climbTree(root);

        return ((double) leaf.getFlipCount() / leaf.getTotalCases() > 0.5) ^ history.getLastRound().getPlayerChoice();
    }


    private Node climbTree(Node node) {
        if (Math.abs(node.entropy()) <= TRESHOLD) {
            return node;
        }
        if (node.getDepth() == Node.MAX_DEPTH) {
            return node;
        }
        int roundNr = history.getNumberOfRounds() - 1 - node.getDepth();
        if (roundNr < 0) {
            return node;
        }
        if (hasFlipped(roundNr)) {
            if (hasWon(roundNr)) {
                if (node.winLastRound == null && node.flipLastRound != null) {
                    return climbTree(node.flipLastRound);
                }
                if (node.winLastRound != null && node.flipLastRound == null) {
                    return climbTree(node.winLastRound);
                }
                if (node.winLastRound == null && node.flipLastRound == null) {
                    return node;
                }
                if (node.flipLastRound.weightedEntropy() < node.winLastRound.weightedEntropy()) {
                    return climbTree(node.flipLastRound);
                } else {
                    return climbTree(node.winLastRound);
                }
            } else {
                if (node.lossLastRound == null && node.flipLastRound != null) {
                    return climbTree(node.flipLastRound);
                }
                if (node.lossLastRound != null && node.flipLastRound == null) {
                    return climbTree(node.lossLastRound);
                }
                if (node.lossLastRound == null && node.flipLastRound == null) {
                    return node;
                }
                if (node.flipLastRound.weightedEntropy() < node.lossLastRound.weightedEntropy()) {
                    return climbTree(node.flipLastRound);
                } else {
                    return climbTree(node.lossLastRound);
                }
            }
        } else {
            if (hasWon(roundNr)) {
                if (node.winLastRound == null && node.nonFlipLastRound != null) {
                    return climbTree(node.nonFlipLastRound);
                }
                if (node.winLastRound != null && node.nonFlipLastRound == null) {
                    return climbTree(node.winLastRound);
                }
                if (node.winLastRound == null && node.nonFlipLastRound == null) {
                    return node;
                }
                if (node.nonFlipLastRound.weightedEntropy() < node.winLastRound.weightedEntropy()) {
                    return climbTree(node.nonFlipLastRound);
                } else {
                    return climbTree(node.winLastRound);
                }
            } else {
                if (node.lossLastRound == null && node.nonFlipLastRound != null) {
                    return climbTree(node.nonFlipLastRound);
                }
                if (node.lossLastRound != null && node.nonFlipLastRound == null) {
                    return climbTree(node.lossLastRound);
                }
                if (node.lossLastRound == null && node.nonFlipLastRound == null) {
                    return node;
                }
                if (node.nonFlipLastRound.weightedEntropy() < node.lossLastRound.weightedEntropy()) {
                    return climbTree(node.nonFlipLastRound);
                } else {
                    return climbTree(node.lossLastRound);
                }
            }
        }
    }

    private boolean hasFlipped(int roundNr) {
        if (roundNr < 1) {
            return false;
        } else {
            return history.getRoundList().get(roundNr).getPlayerChoice() !=
                    history.getRoundList().get(roundNr - 1).getPlayerChoice();
        }
    }

    private boolean hasWon(int roundNr) {
        if (roundNr < 0) {
            return false;
        }
        Round round = history.getRoundList().get(roundNr);
        return (round.getPlayerChoice() != round.getPrediction());
    }

    private class Node {
        private static final int MAX_DEPTH = 5;
        private int depth;
        private int flipCount = 0;
        private int totalCases = 0;
        private Node lossLastRound = null;
        private Node winLastRound = null;
        private Node flipLastRound = null;
        private Node nonFlipLastRound = null;
        private double informationGain;

        public Node(int depth) {
            this.depth = depth;
        }

        public int getDepth() {
            return depth;
        }

        public double weightedEntropy() {
            return entropy() * 1 / totalCases;
        }

        private double log2(double n) {
            if (n == 0) {
                return 0;
            }
            return Math.log(n) / Math.log(2);
        }

        private double entropy() {
            return ((double) -flipCount / totalCases) * log2((double) flipCount / totalCases)
                    - (double) (totalCases - flipCount) / totalCases * log2((double) (totalCases - flipCount) / totalCases);
        }

        private double informationGain() {
            Double lossLastRoundGain = 0.0;
            Double winLastRoundGain = 0.0;
            Double flipLastRoundGain = 0.0;
            Double nonFlipLastRoundGain = 0.0;
            if (lossLastRound != null) {
                lossLastRoundGain = (double) lossLastRound.getTotalCases() / (2 * totalCases) * lossLastRound.entropy();
            }
            if (winLastRound != null) {
                winLastRoundGain = (double) winLastRound.getTotalCases() / (2 * totalCases) * winLastRound.entropy();
            }
            if (flipLastRound != null) {
                flipLastRoundGain = (double) flipLastRound.getTotalCases() / (2 * totalCases) * flipLastRound.entropy();
            }
            if (nonFlipLastRound != null) {
                nonFlipLastRoundGain = (double) nonFlipLastRound.getTotalCases() / (2 * totalCases) * nonFlipLastRound.entropy();
            }

            return entropy() - (lossLastRoundGain + winLastRoundGain + flipLastRoundGain + nonFlipLastRoundGain);
        }

        private Node maxInformationGainNode() {
            Node maxNode = lossLastRound;
            if (winLastRound.informationGain() > maxNode.informationGain()) {
                maxNode = winLastRound;
            }
            if (flipLastRound.informationGain() > maxNode.informationGain()) {
                maxNode = flipLastRound;
            }
            if (nonFlipLastRound.informationGain() > maxNode.informationGain()) {
                maxNode = nonFlipLastRound;
            }
            return maxNode;
        }

        public void update(int roundNr) {
            totalCases++;
            if (hasFlipped(history.getNumberOfRounds() - 1)) {
                flipCount++;
            }
            informationGain = informationGain();
            if (depth == MAX_DEPTH || roundNr < 1) {
                return;
            }
            if (hasWon(roundNr - 1)) {
                if (winLastRound == null) {
                    winLastRound = new Node(depth + 1);
                }
                winLastRound.update(roundNr - 1);
            } else {
                if (lossLastRound == null) {
                    lossLastRound = new Node(depth + 1);
                }
                lossLastRound.update(roundNr - 1);
            }
            if (hasFlipped(roundNr - 1)) {
                if (flipLastRound == null) {
                    flipLastRound = new Node(depth + 1);
                }
                flipLastRound.update(roundNr - 1);
            } else {
                if (nonFlipLastRound == null) {
                    nonFlipLastRound = new Node(depth + 1);
                }
                nonFlipLastRound.update(roundNr - 1);
            }
        }

        public int getFlipCount() {
            return flipCount;
        }

        public int getTotalCases() {
            return totalCases;
        }


    }
}
