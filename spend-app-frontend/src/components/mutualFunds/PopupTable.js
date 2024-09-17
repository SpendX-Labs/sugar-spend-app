import React, { useState } from "react";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import Button from "@mui/material/Button";
import { DataGrid } from "@mui/x-data-grid";
import { Grid, Tooltip, Typography } from "@mui/material";
import { makeStyles } from "@mui/styles";
import AddItemDialog from "./AddItemDialog";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const useStyles = makeStyles(() => ({
    positiveReturn: {
        fontWeight: "bold",
        color: "green",
    },
    negativeReturn: {
        fontWeight: "bold",
        color: "red",
    },
    buttonContainer: {
        display: "flex",
        justifyContent: "flex-end", // Align buttons to the right
        marginTop: "16px", // Add space above the buttons
    },
}));

const PopupTable = (props) => {
    const classes = useStyles();
    const [selectedRow, setSelectedRow] = useState(null);
    const [openAddForm, setOpenAddForm] = React.useState(false);
    const [selectedRowData, setSelectedRowData] = useState(null);

    const handleRowSelection = (rows) => {
        if (rows.length === 3) {
            rows.splice(1, 1);
        }
        if (rows.length === 2) {
            setSelectedRow(rows);
        } else {
            setSelectedRow(null);
        }
    };

    const handleClose = () => {
        setOpenAddForm(false);
    };

    const handleEdit = () => {
        const rowData = props.data.orderDetails.find((item) => item.id === selectedRow[1]);
        setSelectedRowData(rowData);
        setOpenAddForm(true);
    };

    const handleDelete = () => {
        console.log("Delete clicked for row:", selectedRow);
    };

    return (
        <Dialog
            open={props.isOpen}
            onClose={props.onClose}
            fullWidth
            maxWidth="lg"
            PaperProps={{
                style: {
                    width: "70%",
                    height: "90%",
                    maxHeight: "none",
                },
            }}
        >
            <DialogTitle>{props.data.fundName}</DialogTitle>
            <DialogContent>
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Typography variant="body1">
                            <strong>Current Amount:</strong>{" "}
                            <span
                                className={
                                    props.data.returnAmount >= 0
                                        ? classes.positiveReturn
                                        : classes.negativeReturn
                                }
                            >
                                {parseInt(props.data.currentAmount)}
                            </span>
                        </Typography>
                        <Typography variant="body1">
                            <strong>Invested Amount:</strong> {parseInt(props.data.investedAmount)}
                        </Typography>
                        <Typography variant="body1">
                            <strong>Total Units:</strong> {props.data.totalUnits.toFixed(2)}
                        </Typography>
                    </Grid>
                    <Grid item xs={6}>
                        <Typography variant="body1">
                            <strong>Return:</strong>{" "}
                            <span
                                className={
                                    props.data.returnAmount >= 0
                                        ? classes.positiveReturn
                                        : classes.negativeReturn
                                }
                            >
                                {props.data.returnAmount.toFixed(2)} ({props.data.returnPercentage.toFixed(2)}%)
                            </span>
                        </Typography>
                        <Typography variant="body1">
                            <strong>XIRR Value:</strong>{" "}
                            <span
                                className={
                                    props.data.returnAmount >= 0
                                        ? classes.positiveReturn
                                        : classes.negativeReturn
                                }
                            >
                                {props.data.xirrValue}%
                            </span>
                        </Typography>
                        <Typography variant="body1">
                            <strong>Current NAV:</strong> {props.data.currentNav.toFixed(2)}
                        </Typography>
                    </Grid>
                </Grid>
                <div className={classes.buttonContainer} style={{ marginBottom: "16px" }}>
                    <Tooltip title="Edit">
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleEdit}
                            disabled={selectedRow === null}
                        >
                            <EditIcon />
                        </Button>
                    </Tooltip>
                    <Tooltip title="Delete">
                        <Button
                            variant="contained"
                            color="secondary"
                            onClick={handleDelete}
                            disabled={selectedRow === null}
                            style={{ marginLeft: "16px" }}
                        >
                            <DeleteIcon />
                        </Button>
                    </Tooltip>
                </div>
                <DataGrid
                    rows={props.data.orderDetails}
                    columns={props.columData}
                    initialState={{
                        pagination: {
                            paginationModel: { page: 0, pageSize: 5 },
                        },
                        sorting: {
                            sortModel: [
                                {
                                    field: "dateOfEvent",
                                    sort: "desc",
                                },
                            ],
                        },
                    }}
                    // pageSizeOptions={[5, 10]}
                    checkboxSelection
                    autoHeight
                    onRowSelectionModelChange={(newSelection) => {
                        handleRowSelection(newSelection);
                    }}
                    rowSelectionModel={selectedRow ? selectedRow : {}}
                    style={{ marginBottom: "16px" }}
                />
            </DialogContent>

            <AddItemDialog open={openAddForm} onClose={handleClose} mutualFundData={props.mutualFundData}
                updateData={props.updateData} setAddLoader={props.setAddLoader} selectedRowData={selectedRowData} itemType="Edit" />
        </Dialog>
    );
};

export default PopupTable;
