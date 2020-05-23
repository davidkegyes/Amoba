package edu.sapi.mestint;


public class AmobaSolver {
    public static final int X = 0;
    public static final int O = 1;

    public static final int MIN = 0;
    public static final int MAX = 1;

    private Amoba amoba;
    private int dimension;

    private int depth = -1;
    private int I;
    private int J;

    public AmobaSolver(Amoba amoba) {
        this.amoba = amoba;
        this.dimension = amoba.getDimension();
    }

    public double putOnTable(int step, int type, double topValue, boolean isTopValueSet, int maxDepth, boolean useMaxDepth) {
        depth++;
        double value = 0;
        boolean isValueSet = false;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (amoba.isEmpty(i, j)) {
                    if (step == X) {
                        amoba.placeElement(i, j, Amoba.X);
                    } else if (step == O) {
                        amoba.placeElement(i, j, Amoba.O);
                    }

                    if (!amoba.isSolved()) {
                        double tempValue;

                        if (useMaxDepth && depth >= maxDepth) {
                            tempValue = getSignedValue(amoba.getEstimatedValue(i, j), type);
                        } else {
                            tempValue = putOnTable(whosNext(step), whatType(type), value, isValueSet, maxDepth, useMaxDepth);
                        }
                        if (!isValueSet) {
                            value = tempValue;
                            isValueSet = true;
                            saveIndex(i, j);
                        } else {
                            if (type == MAX) {
                                if (tempValue > value) {
                                    value = tempValue;
                                    saveIndex(i, j);
                                }
                            } else if (type == MIN) {
                                if (tempValue < value) {
                                    value = tempValue;
                                    saveIndex(i, j);
                                }
                            }
                        }
                    } else {
                        if (type == MAX) {
                            value = 1;
                            saveIndex(i, j);
                        } else if (type == MIN) {
                            value = -1;
                            saveIndex(i, j);
                        }

                        isValueSet = true;
                    }

                    amoba.placeElement(i, j, Amoba.EMPTY);

                    //The alpha - beta cuts.
                    if (isTopValueSet && isValueSet && type == MAX) {
                        if (value >= topValue) {
                            depth--;
                            return value;
                        }
                    } else if (isTopValueSet && isValueSet && type == MIN) {
                        if (value <= topValue) {
                            depth--;
                            return value;
                        }
                    }
                }
            }
        }

        placeOnTable(step);
        depth--;
        return value;
    }

    public void saveIndex(int i, int j) {
        if (depth == 0) {
            I = i;
            J = j;
        }
    }

    private void placeOnTable(int step) {
        if (depth == 0) {
            if (step == X) {
                amoba.placeElement(I, J, Amoba.X);
            } else if (step == O) {
                amoba.placeElement(I, J, Amoba.O);
            }
        }
    }

    private int whosNext(int current) {
        return (current + 1) % 2;
    }

    private int whatType(int type) {
        return (type + 1) % 2;
    }

    private double getSignedValue(double tempValue, int type) {
        if (type == MIN) {
            return (-1) * tempValue;
        }

        return tempValue;
    }
}
