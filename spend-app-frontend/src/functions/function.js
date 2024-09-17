export function formatDate(dateString) {
  const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
  const date = new Date(dateString);
  return date.toLocaleDateString('en-GB', options);
}

export function formatTime(timeString) {
  const options = { hour: '2-digit', minute: '2-digit' };
  const time = new Date(`2000-01-01T${timeString}`);
  return time.toLocaleTimeString('en-GB', options);
}

export function getLastThreeYears() {
  const today = new Date();
  const recentYear = today.getFullYear();
  const yearsList = Array.from({ length: 3 }, (_, index) => recentYear - index);
  return yearsList;
}