import { Dialog, DialogTitle, FormControl, styled } from "@mui/material";

export const DialogPaper = styled(Dialog)(({ theme }) => ({
    width: '80%',
    maxWidth: '900px',
    padding: theme.spacing(3),
    margin: 'auto',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    height: '120vh',
}));

export const DialogHeader = styled(DialogTitle)(({ theme }) => ({
    backgroundColor: theme.palette.primary.main,
    color: theme.palette.primary.contrastText,
    padding: theme.spacing(2),
    textAlign: 'center',
}));

export const ModifyContainerDetails = styled('div')(({ theme }) => ({
    padding: theme.spacing(2),
    borderRadius: theme.shape.borderRadius,
    boxShadow: theme.shadows[1],
    marginBottom: theme.spacing(2), // Added margin
}));

export const FormControlStyled = styled(FormControl)(({ theme }) => ({
    marginBottom: theme.spacing(2), // Added margin
}));

export const ButtonGroup = styled('div')(({ theme }) => ({
    marginTop: theme.spacing(2),
}));