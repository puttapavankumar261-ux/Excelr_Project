import {
  FiAlertCircle,
  FiBarChart2,
  FiDatabase,
  FiLoader,
  FiRefreshCw,
  FiSearch,
} from "react-icons/fi";
import { normalizeStatus } from "./formatters";

export function EnterprisePage({ children, className = "" }) {
  return <div className={`enterprise-page ${className}`}>{children}</div>;
}

export function PageHero({
  eyebrow,
  title,
  description,
  actions,
  meta,
  icon: Icon = FiBarChart2,
}) {
  return (
    <section className="enterprise-hero">
      <div className="enterprise-hero-copy">
        {eyebrow && <span className="enterprise-eyebrow">{eyebrow}</span>}
        <h1>{title}</h1>
        {description && <p>{description}</p>}
        {meta && <div className="enterprise-hero-meta">{meta}</div>}
      </div>

      <div className="enterprise-hero-side">
        <span className="enterprise-hero-icon">
          <Icon />
        </span>
        {actions && <div className="enterprise-hero-actions">{actions}</div>}
      </div>
    </section>
  );
}

export function MetricCard({ label, value, helper, icon: Icon, tone = "blue" }) {
  return (
    <article className={`enterprise-metric metric-${tone}`}>
      <span className="enterprise-metric-icon">{Icon && <Icon />}</span>
      <div>
        <span>{label}</span>
        <strong>{value}</strong>
        {helper && <small>{helper}</small>}
      </div>
    </article>
  );
}

export function ErrorBanner({ message, onRetry }) {
  if (!message) return null;

  return (
    <div className="enterprise-alert" role="alert">
      <FiAlertCircle />
      <span>{message}</span>
      {onRetry && (
        <button type="button" onClick={onRetry}>
          <FiRefreshCw /> Retry
        </button>
      )}
    </div>
  );
}

export function LoadingState({ label = "Loading data..." }) {
  return (
    <div className="enterprise-state">
      <FiLoader className="enterprise-spinner" />
      <strong>{label}</strong>
    </div>
  );
}

export function EmptyState({ title = "No records found", message }) {
  return (
    <div className="enterprise-state">
      <FiDatabase />
      <strong>{title}</strong>
      {message && <span>{message}</span>}
    </div>
  );
}

export function StatusBadge({ status }) {
  const rawStatus = String(status || "UNKNOWN");
  const key = rawStatus.toLowerCase();
  const tone =
    key.includes("approved") || key.includes("present") || key.includes("active")
      ? "success"
      : key.includes("reject") || key.includes("absent") || key.includes("delete")
        ? "danger"
        : key.includes("pending") || key.includes("review") || key.includes("leave")
          ? "warning"
          : key.includes("cancel") || key.includes("inactive")
            ? "neutral"
            : "info";

  return (
    <span className={`enterprise-status status-${tone}`}>
      {normalizeStatus(rawStatus)}
    </span>
  );
}

export function SearchField({ value, onChange, placeholder, label = "Search" }) {
  return (
    <label className="enterprise-search">
      <span className="visually-hidden">{label}</span>
      <FiSearch />
      <input
        type="search"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder={placeholder}
      />
    </label>
  );
}

export function MiniBarChart({ items, valueLabel = "records" }) {
  const maxValue = Math.max(...items.map((item) => Number(item.value || 0)), 1);

  return (
    <div className="enterprise-bars" aria-label="Data distribution">
      {items.map((item) => {
        const width = Math.max(4, Math.round((Number(item.value || 0) / maxValue) * 100));

        return (
          <div className="enterprise-bar-row" key={item.label}>
            <div>
              <strong>{item.label}</strong>
              <span>
                {item.value} {valueLabel}
              </span>
            </div>
            <div className="enterprise-bar-track">
              <span style={{ width: `${width}%` }} />
            </div>
          </div>
        );
      })}
    </div>
  );
}
