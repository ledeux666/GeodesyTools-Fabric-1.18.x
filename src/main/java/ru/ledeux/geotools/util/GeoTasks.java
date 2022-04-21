package ru.ledeux.geotools.util;

public class GeoTasks {

    // Прямоугольные координаты трех основных точек
    private double[] baseXYZ = new double[3];
    private double[] initXYZ = new double[3];
    private double[] finalXYZ = new double[3];

    // TODO: Реализовать изменение baseHeight и aimHeight

    // Разность прямоугольных координат точек и базы
    private double[] diffInit = new double[3];
    private double[] diffFinal = new double[3];

    // Наклонные и горизонтальные расстояния до начальной и искомой точки
    private double initDistance;
    private double initHorizontalDistance;
    private double finalDistance;
    private double finalHorizontalDistance;

    // Превышение (для обратной геодезической задачи)
    private double elevation;

    // Румбы, азимуты направлений и горизонтальный угол
    private double initRhumb;
    private double finalRhumb;
    private double initAzimuth;
    private double finalAzimuth;
    private double horizontalAngle;

    // Вертикальные направления и угол
    private double initVerticalDir;
    private double finalVerticalDir;
    private double verticalAngle;

    // Прямая геодезическая задача (получение координат искомого пункта)
    public void directTask() {

        inverseTask();
        findDiffFinalXYZ();
        findFinalDistance();
        findFinalRhumb();
        finalRhumbToAzimuth();
        findHorizontalAngle();
        findInitVerticalDir();
        findFinalVerticalDir();
        findVerticalAngle();
    }

    // Обратная геодезическая задача (получение азимута направления, расстояний и превышения)
    public void inverseTask() {

        findDiffInitXYZ();
        findInitDistance();
        findInitRhumb();
        initRhumbToAzimuth();
        elevation = diffInit[1];
    }

    // Разница координат начальной точки и базы
    public void findDiffInitXYZ() {

        for (int i = 0; i < 3; i++) {
            diffInit[i] = initXYZ[i] - baseXYZ[i];
        }
    }

    // Разница координат конечного пункта и базы
    public void findDiffFinalXYZ() {

        for (int i = 0; i < 3; i++) {
            diffFinal[i] = finalXYZ[i] - baseXYZ[i];
        }
    }

    // Расчет расстояния и горизонтального проложения до начальной точки
    public void findInitDistance() {

        initDistance = Math.sqrt(Math.pow(diffInit[0], 2) + Math.pow(diffInit[1], 2) + Math.pow(diffInit[2], 2));
        initHorizontalDistance = Math.sqrt(Math.pow(diffInit[0], 2) + Math.pow(diffInit[2], 2));
    }

    // Расчет расстояния и горизонтального проложения до конечной точки
    public void findFinalDistance() {

        finalDistance = Math.sqrt(Math.pow(diffFinal[0], 2) + Math.pow(diffFinal[1], 2) + Math.pow(diffFinal[2], 2));
        finalHorizontalDistance = Math.sqrt(Math.pow(diffFinal[0], 2) + Math.pow(diffFinal[2], 2));
    }

    // Расчет румба начального направления
    public void findInitRhumb() {

        if (diffInit[0] == 0) {
            initRhumb = 0;
        } else if (diffInit[2] == 0) {
            initRhumb = Math.toDegrees(Math.PI / 2);
        } else {
            initRhumb = Math.toDegrees(Math.atan2(Math.abs(diffInit[0]), Math.abs(diffInit[2])));
        }
    }

    // Расчет румба конечного направления
    public void findFinalRhumb() {

        if (diffFinal[0] == 0) {
            finalRhumb = 0;
        } else if (diffFinal[2] == 0) {
            finalRhumb = Math.toDegrees(Math.PI / 2);
        } else {
            finalRhumb = Math.toDegrees(Math.atan2(Math.abs(diffFinal[0]), Math.abs(diffFinal[2])));
        }
    }

    // Переход от румба начального направления к азимуту
    public void initRhumbToAzimuth() {

        if ((diffInit[0] <= 0) && (diffInit[2] > 0)) {
            initAzimuth = initRhumb;
        }
        if ((diffInit[0] < 0) && (diffInit[2] <= 0)) {
            initAzimuth = Math.toDegrees(Math.PI - Math.toRadians(initRhumb));
        }
        if ((diffInit[0] >= 0) && (diffInit[2] < 0)) {
            initAzimuth = Math.toDegrees(- (Math.PI - Math.toRadians(initRhumb)));
        }
        if ((diffInit[0] > 0) && (diffInit[2] >= 0)) {
            initAzimuth = - initRhumb;
        }
    }

    // Переход от румба конечного направления к азимуту
    public void finalRhumbToAzimuth() {

        if ((diffFinal[0] <= 0) && (diffFinal[2] > 0)) {
            finalAzimuth = finalRhumb;
        }
        if ((diffFinal[0] < 0) && (diffFinal[2] <= 0)) {
            finalAzimuth = Math.toDegrees(Math.PI - Math.toRadians(finalRhumb));
        }
        if ((diffFinal[0] >= 0) && (diffFinal[2] < 0)) {
            finalAzimuth = Math.toDegrees(- (Math.PI - Math.toRadians(finalRhumb)));
        }
        if ((diffFinal[0] > 0) && (diffFinal[2] >= 0)) {
            finalAzimuth = - finalRhumb;
        }
    }

    // Расчет вертикального направления на начальную точку
    public void findInitVerticalDir() {

        double horizontalDistance = Math.sqrt(Math.pow(diffInit[0], 2) + Math.pow(diffInit[2], 2));
        if (diffInit[1] <= 0) {
            initVerticalDir = Math.toDegrees(Math.atan2(Math.abs(diffInit[1]), horizontalDistance));
        } else {
            initVerticalDir = Math.toDegrees(- Math.atan2(Math.abs(diffInit[1]), horizontalDistance));
        }
    }

    // Расчет вертикального направления на конечную точку
    public void findFinalVerticalDir() {

        double horizontalDistance = Math.sqrt(Math.pow(diffFinal[0], 2) + Math.pow(diffFinal[2], 2));
        if (diffFinal[1] <= 0) {
            finalVerticalDir = Math.toDegrees(Math.atan2(Math.abs(diffFinal[1]), horizontalDistance));
        } else {
            finalVerticalDir = Math.toDegrees(- Math.atan2(Math.abs(diffFinal[1]), horizontalDistance));
        }
    }

    // Расчет горизонтального угла между направлениями
    public void findHorizontalAngle() {
        horizontalAngle = finalAzimuth - initAzimuth;
    }

    // Расчет вертикального угла между направлениями
    public void findVerticalAngle() {
        verticalAngle = finalVerticalDir - initVerticalDir;
    }

    // Геттеры и сеттеры
    public double[] getBaseXYZ() {
        return baseXYZ;
    }

    public void setBaseXYZ(double[] baseXYZ) {
        this.baseXYZ = baseXYZ;
    }

    public double[] getInitXYZ() {
        return initXYZ;
    }

    public void setInitXYZ(double[] initXYZ) {
        this.initXYZ = initXYZ;
    }

    public double[] getFinalXYZ() {
        return finalXYZ;
    }

    public void setFinalXYZ(double[] finalXYZ) {
        this.finalXYZ = finalXYZ;
    }

    public double getInitDistance() {
        return initDistance;
    }

    public double getInitHorizontalDistance() {
        return initHorizontalDistance;
    }

    public double getFinalDistance() {
        return finalDistance;
    }

    public double getFinalHorizontalDistance() {
        return finalHorizontalDistance;
    }

    public double getElevation() {
        return elevation;
    }

    public double[] getDiffInit() {
        return diffInit;
    }

    public double[] getDiffFinal() {
        return diffFinal;
    }

    public double getInitRhumb() {
        return initRhumb;
    }

    public double getFinalRhumb() {
        return finalRhumb;
    }

    public double getInitAzimuth() {
        return initAzimuth;
    }

    public double getFinalAzimuth() {
        return finalAzimuth;
    }

    public double getHorizontalAngle() {
        return horizontalAngle;
    }

    public double getInitVerticalDir() {
        return initVerticalDir;
    }

    public double getFinalVerticalDir() {
        return finalVerticalDir;
    }

    public double getVerticalAngle() {
        return verticalAngle;
    }
}
