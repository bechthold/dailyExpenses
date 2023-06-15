package de.ibechthold.bidailyexpenses.model;

public class Balance {
    //region constants
    //endregion

    //region attributes
    private Double incomes;
    private Double expenses;
    //endregion

    //region constructors

    public Balance() {
        incomes = 0d;
        expenses = 0d;
    }

    public Balance(Double incomes, Double expenses) {
        this.incomes = incomes;
        this.expenses = expenses;
    }

    //endregion

    //region methods

    public Double getIncomes() {
        return incomes;
    }

    public void setIncomes(Double incomes) {
        this.incomes = incomes;
    }

    public Double getExpenses() {
        return expenses;
    }

    public void setExpenses(Double expenses) {
        this.expenses = expenses;
    }

    //endregion
}
