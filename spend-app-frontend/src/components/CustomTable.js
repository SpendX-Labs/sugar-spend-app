import * as React from "react";
import PropTypes from "prop-types";
import Box from "@mui/material/Box";
import Collapse from "@mui/material/Collapse";
import IconButton from "@mui/material/IconButton";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Typography from "@mui/material/Typography";
import Paper from "@mui/material/Paper";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  positivePercentage: {
    fontWeight: "bold",
    fontSize: "12",
    backgroundColor: "green",
    color: "white",
  },
  negativePercentage: {
    fontWeight: "bold",
    fontSize: "12",
    backgroundColor: "red",
    color: "white",
  },
  table: {
    minWidth: 650,
    "& .MuiTableCell-root": {
      border: "1px solid black",
    },
  },
}));

function Row(props) {
  const classes = useStyles();
  const [open, setOpen] = React.useState(false);

  return (
    <React.Fragment>
      <TableRow sx={{ "& > *": { borderBottom: "unset" } }}>
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="small"
            onClick={() => setOpen(!open)}
          >
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        {props.columns.map((col) => {
          const cellValue = props.row[col.field];

          const cellClassName = col.cellStyle
            ? cellValue > 0
              ? classes.positivePercentage
              : classes.negativePercentage
            : null;
          const finalVlaue =
            col.suffix !== undefined
              ? `${props.row[col.field]}` + col.suffix
              : props.row[col.field] !== undefined
              ? `${props.row[col.field]}`
              : "-";
          return (
            <TableCell key={col.field} align="right" className={cellClassName}>
              {finalVlaue}
            </TableCell>
          );
        })}
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <Typography variant="h6" gutterBottom component="div">
                Order Details
              </Typography>
              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    {props.internalColumns.map((col) => (
                      <TableCell key={col.field} align="right">
                        {col.headerName}
                      </TableCell>
                    ))}
                  </TableRow>
                </TableHead>
                <TableBody>
                  {props.row.details.map((detail) => (
                    <TableRow key={detail[props.internalRowKey]}>
                      {Object.values(detail).map((value) => (
                        <TableCell key={value} align="right">
                          {value}
                        </TableCell>
                      ))}
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </React.Fragment>
  );
}

Row.propTypes = {
  row: PropTypes.shape({}).isRequired,
  internalRowKey: PropTypes.string.isRequired,
  excludeColumn: PropTypes.string.isRequired,
};

const CustomTable = (props) => {
  const classes = useStyles();
  const [secondColumns, setSecondColumns] = React.useState([]);

  const getMainColumns = (data) => {
    const mainColumns = [];
    data.forEach((element) => {
      mainColumns.push(...element.subHeaders);
    });
    return mainColumns;
  };

  React.useEffect(() => {
    setSecondColumns(getMainColumns(props.columns));
  }, []);
  return (
    <Box sx={{ width: "100%" }}>
      <TableContainer component={Paper}>
        <Table className={classes.table} aria-label="collapsible table">
          <TableHead>
            <TableRow>
              <TableCell colSpan={1} />
              {props.columns.map((col) => (
                <TableCell align="center" colSpan={col.subHeaders.length}>
                  {col.headerName}
                </TableCell>
              ))}
            </TableRow>
            <TableRow>
              <TableCell />
              {secondColumns.map((col) => (
                <TableCell key={col.field} align="right">
                  {col.headerName}
                </TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {props.rows.map((row) => (
              <Row
                key={row[props.rowKey]}
                columns={secondColumns}
                row={row}
                internalColumns={props.internalColumns}
                internalRowKey={props.internalRowKey}
                excludeColumn={props.excludeColumn}
              />
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

CustomTable.propTypes = {
  columns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  rows: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  internalColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  excludeColumn: PropTypes.string.isRequired,
  rowKey: PropTypes.string.isRequired,
  internalRowKey: PropTypes.string.isRequired,
};

export default CustomTable;
