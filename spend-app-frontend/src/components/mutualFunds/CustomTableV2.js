import React, { useState } from "react";
import PropTypes from "prop-types";
import Box from "@mui/material/Box";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { makeStyles } from "@mui/styles";
import PopupTable from "./PopupTable";
import { TablePagination } from "@mui/material";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";


const useStyles = makeStyles(() => ({
    positivePercentage: {
        fontWeight: "bold",
        fontSize: "12",
        backgroundColor: "#306844",
        color: "white",
    },
    negativePercentage: {
        fontWeight: "bold",
        fontSize: "12",
        backgroundColor: "#bf0000",
        color: "white",
    },
    table: {
        minWidth: 650,
        "& .MuiTableCell-root": {
            border: "1px solid black",
            transition: "background-color 0.3s ease",
        },
        "& .MuiTableRow-root.selected": {
            backgroundColor: "#716d6d",
        },
    },
    hoveredCell: {
        backgroundColor: "#716d6d",
    },
}));

function Row(props) {
    const classes = useStyles();
    const [isHovered, setIsHovered] = useState(false);
    const [isSelected, setIsSelected] = useState(false);

    const convertValue = (field, value) => {
        if (!value) {
            return "-";
        }
        const column = props.columns.find((col) => col.field === field);
        if (column && column.convertToInteger) {
            return parseInt(value);
        }
        else if (column && column.convertToTwoDeciaml) {
            return value.toFixed(2);
        }
        return value;
    };

    const handleRowClick = () => {
        setIsSelected(!isSelected);
    };

    return (
        <React.Fragment>
            <TableRow
                sx={{ "& > *": { borderBottom: "unset" } }}
                onMouseEnter={() => setIsHovered(true)}
                onMouseLeave={() => setIsHovered(false)}
                className={isSelected ? "selected" : ""}
                onClick={handleRowClick}
            >
                {props.columns.map((col) => {
                    let cellValue = props.row[col.field];
                    const colorCellValue = col.colorCell === undefined ? cellValue : props.row[col.colorCell];
                    if(col.cellValue !== undefined){
                        cellValue = col.cellValue(props.row);
                    }

                    const cellClassName = !cellValue && !colorCellValue ? null :
                        (col.cellStyle ? colorCellValue > 0
                            ? classes.positivePercentage
                            : classes.negativePercentage
                            : null);

                    cellValue = cellValue !== undefined ? convertValue(col.field, cellValue) : "-";

                    const finalVlaue =
                        col.suffix !== undefined && cellValue !== "-"
                            ? `${cellValue}` + col.suffix
                            : cellValue !== undefined
                                ? `${cellValue}`
                                : "-";
                    return (
                        <TableCell
                            key={col.field}
                            align="right"
                            className={`${cellClassName} ${cellClassName === null && isHovered ? classes.hoveredCell : ""
                                }`}
                            onDoubleClick={() => props.onCellDoubleClick(props.row)}
                        >
                            {finalVlaue}
                        </TableCell>
                    );
                })}
            </TableRow>
        </React.Fragment>
    );
}

Row.propTypes = {
    row: PropTypes.shape({}).isRequired,
};

const CustomTableV2 = (props) => {
    const classes = useStyles();
    const [secondColumns, setSecondColumns] = React.useState([]);
    const [popupOpen, setPopupOpen] = useState(false);
    const [selectedRowData, setSelectedRowData] = useState({});
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(5);
    const [sortBy, setSortBy] = useState(props.sortBy);
    const [sortOrder, setSortOrder] = useState("desc");
    const [isHistoricalDataExpanded, setIsHistoricalDataExpanded] = useState(false);

    const toggleHistoricalData = () => {
        setIsHistoricalDataExpanded(!isHistoricalDataExpanded);
      };

    const getMainColumns = (data) => {
        const mainColumns = [];
        data.forEach((element) => {
            mainColumns.push(...element.subHeaders);
        });
        return mainColumns;
    };

    const handleCellDoubleClick = (row) => {
        setSelectedRowData(row);
        setPopupOpen(true);
    };

    const handleClosePopup = () => {
        setPopupOpen(false);
        setSelectedRowData({});
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleColumnHeaderClick = (column) => {
        if (sortBy === column.field) {
            setSortOrder(sortOrder === "asc" ? "desc" : "asc");
        } else {
            setSortBy(column.field);
            setSortOrder("asc");
        }
    };

    const sortedRows = [...props.rows].sort((a, b) => {
        const aValue = a[sortBy];
        const bValue = b[sortBy];

        if (typeof aValue === "number" && typeof bValue === "number") {
            return sortOrder === "asc" ? aValue - bValue : bValue - aValue;
        } else {
            return sortOrder === "asc"
                ? aValue.toString().localeCompare(bValue.toString())
                : bValue.toString().localeCompare(aValue.toString());
        }
    });

    const startIndex = page * rowsPerPage;
    const endIndex = startIndex + rowsPerPage;

    const visibleRows = sortedRows.slice(startIndex, endIndex);

    React.useEffect(() => {
        setSecondColumns(getMainColumns(props.columns));
    }, []);
    return (
        <Box sx={{ width: "100%" }}>
            <TableContainer component={Paper}>
                <Table className={classes.table} aria-label="collapsible table">
                    <TableHead>
                        <TableRow>
                            {props.columns.map((col) => (
                                <TableCell key={col.headerName} align="center" colSpan={col.subHeaders.length}>
                                    {col.headerName === "Historical Data" && (
                                        <span
                                            onClick={toggleHistoricalData}
                                            style={{ marginLeft: "5px", cursor: "pointer" }}
                                        >
                                            {isHistoricalDataExpanded ? (
                                                <RemoveCircleOutlineIcon />
                                            ) : (
                                                <AddCircleOutlineIcon />
                                            )}
                                        </span>
                                    )}
                                    {col.headerName}
                                </TableCell>
                            ))}
                        </TableRow>
                        <TableRow>
                            {secondColumns.map((col) => (
                                <TableCell key={col.field}
                                    align="right"
                                    onClick={() => handleColumnHeaderClick(col)}
                                >
                                    {col.headerName} {sortBy === col.field && (
                                        <span>{sortOrder === "asc" ? "▲" : "▼"}</span>
                                    )}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {visibleRows.map((row) => (
                            <Row
                                key={row[props.rowKey]}
                                columns={secondColumns}
                                row={row}
                                internalColumns={props.internalColumns}
                                colorCell={props.colorCell}
                                onCellDoubleClick={handleCellDoubleClick}
                            />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <TablePagination
                rowsPerPageOptions={[5, 10, 25, 50, 100]}
                component="div"
                count={props.rows.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />

            {popupOpen &&
                <PopupTable
                    isOpen={popupOpen}
                    onClose={handleClosePopup}
                    data={selectedRowData}
                    columData={props.internalColumns}
                    mutualFundData={props.mutualFundData}
                    updateData={props.updateData}
                    setAddLoader={props.setAddLoader}
                />
            }

        </Box>
    );
};

CustomTableV2.propTypes = {
    columns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
    rows: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
    internalColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
    rowKey: PropTypes.string.isRequired,
};

export default CustomTableV2;
